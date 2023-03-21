package biz.digissance.graalvmdemo.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import biz.digissance.graalvmdemo.domain.DomainPersonRepository;
import biz.digissance.graalvmdemo.domain.PersonRepository;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.address.AddressMapper;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressRepository;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import jakarta.persistence.EntityManager;
import java.util.Set;
import javax.sql.DataSource;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.address.EmailAddress;
import net.liccioni.archetypes.address.GeographicAddress;
import net.liccioni.archetypes.party.Person;
import net.liccioni.archetypes.party.PersonName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "biz.digissance.graalvmdemo.jpa")
class JpaPersonRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JpaPersonRepository jpaPersonRepository;
    @Autowired
    private JpaAddressRepository jpaAddressRepository;

    @Autowired
    private PartyMapper partyMapper;

    @Autowired
    private AddressMapper addressMapper;
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new DomainPersonRepository(jpaPersonRepository, partyMapper);
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(jpaPersonRepository).isNotNull();
        assertThat(jpaAddressRepository).isNotNull();
        assertThat(addressMapper).isNotNull();
    }

    @Test
    void shouldSavePerson() {

        final var gus = Person.builder()
                .personName(PersonName.builder().givenName("Gustavo")
                        .familyName("Rodriguez")
                        .build())
                .build();
        final var created = personRepository.save(gus);
        assertThat(created).usingRecursiveComparison().ignoringFields("partyIdentifier").isEqualTo(gus);
        assertThat(created.getPartyIdentifier()).isNotNull();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldSavePersonAndAddress() {

        final var gus = Person.builder()
                .personName(PersonName.builder().givenName("Gustavo")
                        .familyName("Rodriguez")
                        .build())
                .addressProperties(Set.of(AddressProperties.builder()
                        .use(Set.of("personal", "work"))
                        .address(EmailAddress.builder()
                                .emailAddress("liccioni@gmail.com")
                                .build())
                        .build()))
                .build();
        final var created = personRepository.save(gus);
        assertThat(created).usingRecursiveComparison().ignoringFields("partyIdentifier").isEqualTo(gus);
        assertThat(created.getPartyIdentifier()).isNotNull();
        assertThat(created.getPartyIdentifier().getId()).isNotNull();
    }

    @Test
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldModifyAddress() {

        final var gus = Person.builder()
                .personName(PersonName.builder().givenName("Gustavo")
                        .familyName("Rodriguez")
                        .build())
                .addressProperties(Set.of(AddressProperties.builder()
                                .use(Set.of("email"))
                                .address(EmailAddress.builder()
                                        .emailAddress("liccioni@gmail.com")
                                        .build())
                                .build(),
                        AddressProperties.builder()
                                .use(Set.of("home"))
                                .address(GeographicAddress.builder()
                                        .city("Barcelona")
                                        .build())
                                .build(),
                        AddressProperties.builder()
                                .use(Set.of("work"))
                                .address(GeographicAddress.builder()
                                        .city("Barcelona")
                                        .zipOrPostCode("08018")
                                        .build())
                                .build()))
                .build();
        final var created = personRepository.save(gus);
        entityManager.flush();
        final var oldHomeAddress = created.getAddressProperties().stream()
                .filter(p -> p.getUse().contains("home")).findFirst().orElseThrow();
        final var newHomeAddress = oldHomeAddress.toBuilder()
                .address(GeographicAddress.builder()
                        .addressLine(Set.of("Marroc 11"))
                        .regionOrState("Barcelona")
                        .city("Barcelona")
                        .zipOrPostCode("08018")
                        .build())
                .build();
        personRepository.modifyAddress(created.getPartyIdentifier().getId(),
                addressProperties -> addressProperties.getUse().contains("home"), newHomeAddress);
        entityManager.flush();
        final var actual = personRepository.findByIdentifier(created.getPartyIdentifier().getId()).orElseThrow();
        created.getAddressProperties().removeIf(p -> p.getUse().contains("home"));
        created.getAddressProperties().add(newHomeAddress);
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(created);
    }

    @Test
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldFindPersonById() {

        final var gus = Person.builder()
                .personName(PersonName.builder().givenName("Gustavo")
                        .familyName("Rodriguez")
                        .build())
                .addressProperties(Set.of(AddressProperties.builder()
                        .address(EmailAddress.builder()
                                .emailAddress("liccioni@gmail.com")
                                .build())
                        .build()))
                .build();
        final var created = personRepository.save(gus);
        final var actual = personRepository.findByIdentifier(created.getPartyIdentifier().getId()).orElseThrow();
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(created);
    }

    @Test
    void shouldRemoveAddress() {

        final var gus = Person.builder()
                .personName(PersonName.builder().givenName("Gustavo")
                        .familyName("Rodriguez")
                        .build())
                .addressProperties(Set.of(AddressProperties.builder()
                                .use(Set.of("email"))
                                .address(EmailAddress.builder()
                                        .emailAddress("liccioni@gmail.com")
                                        .build())
                                .build(),
                        AddressProperties.builder()
                                .use(Set.of("home"))
                                .address(GeographicAddress.builder()
                                        .city("Barcelona")
                                        .build())
                                .build()))
                .build();
        final var created = personRepository.save(gus);
        assertThat(created).usingRecursiveComparison().ignoringFields("partyIdentifier").isEqualTo(gus);
        final var homeAddress = created.getAddressProperties().stream().filter(p -> p.getUse().contains("home"))
                .findFirst().orElseThrow();
        created.getAddressProperties().removeIf(p -> p.equals(homeAddress));
        personRepository.removeAddress(created.getPartyIdentifier().getId(),
                addressProperties -> addressProperties.getUse().contains("home"));
        final var actual = personRepository.findByIdentifier(created.getPartyIdentifier().getId()).orElseThrow();
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(created);
    }

    @Test
    void shouldAddAddress() {

        final var gus = Person.builder()
                .personName(PersonName.builder().givenName("Gustavo")
                        .familyName("Rodriguez")
                        .build())
                .addressProperties(Set.of(AddressProperties.builder()
                                .use(Set.of("email"))
                                .address(EmailAddress.builder()
                                        .emailAddress("liccioni@gmail.com")
                                        .build())
                                .build(),
                        AddressProperties.builder()
                                .use(Set.of("work"))
                                .address(GeographicAddress.builder()
                                        .city("Barcelona")
                                        .zipOrPostCode("08018")
                                        .build())
                                .build()))
                .build();
        final var created = personRepository.save(gus);
        entityManager.flush();
        final var newHomeAddress =
                AddressProperties.builder()
                        .use(Set.of("home"))
                        .address(GeographicAddress.builder()
                                .addressLine(Set.of("Marroc 11"))
                                .regionOrState("Barcelona")
                                .city("Barcelona")
                                .zipOrPostCode("08018")
                                .build())
                        .build();
        personRepository.addAddress(created.getPartyIdentifier().getId(), newHomeAddress);
        entityManager.flush();
        final var actual = personRepository.findByIdentifier(created.getPartyIdentifier().getId()).orElseThrow();
        created.getAddressProperties().add(newHomeAddress);
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(created);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldFindPersonByIdWithEntityGraph() {

        final var gus = Person.builder()
                .personName(PersonName.builder().givenName("Gustavo")
                        .familyName("Rodriguez")
                        .build())
                .addressProperties(Set.of(AddressProperties.builder()
                        .use(Set.of("email"))
                        .address(EmailAddress.builder()
                                .emailAddress("liccioni@gmail.com")
                                .build())
                        .build()))
                .build();
        final var created = personRepository.save(gus);
        final var actual = personRepository.findByIdentifier(created.getPartyIdentifier().getId()).orElseThrow();
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(created);
        jpaPersonRepository.deleteAll();
    }
}