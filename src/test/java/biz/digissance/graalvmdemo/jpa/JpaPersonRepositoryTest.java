package biz.digissance.graalvmdemo.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import biz.digissance.graalvmdemo.domain.party.DomainPersonRepository;
import biz.digissance.graalvmdemo.domain.party.PersonRepository;
import biz.digissance.graalvmdemo.domain.party.authentication.EmailPasswordAuthentication;
import biz.digissance.graalvmdemo.domain.party.authentication.OidcAuthentication;
import biz.digissance.graalvmdemo.jpa.party.AddressMapper;
import biz.digissance.graalvmdemo.jpa.party.JpaPartyRepository;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressRepository;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleType;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleTypeRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.address.EmailAddress;
import net.liccioni.archetypes.address.GeographicAddress;
import net.liccioni.archetypes.address.ISOCountryCode;
import net.liccioni.archetypes.address.Locale;
import net.liccioni.archetypes.party.Person;
import net.liccioni.archetypes.party.PersonName;
import net.liccioni.archetypes.relationship.PartyRole;
import net.liccioni.archetypes.relationship.PartyRoleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    private JpaPartyRepository jpaPartyRepository;
    @Autowired
    private JpaAddressRepository jpaAddressRepository;
    @Autowired
    private JpaPartyRoleTypeRepository partyRoleTypeRepository;

    @Autowired
    private JpaPartyAuthenticationRepository jpaAuthenticationRepository;

    @Autowired
    private PartyMapper partyMapper;

    @Autowired
    private AddressMapper addressMapper;
    private PersonRepository personRepository;

    private final Person someGuy = Person.builder()
            .personName(PersonName.builder().givenName("Gustavo")
                    .familyName("Rodriguez")
                    .build())
            .build();

    private final AddressProperties personalEmail = AddressProperties.builder()
            .use(Set.of("personal", "email"))
            .address(EmailAddress.builder().emailAddress("my_email@gmail.com").build())
            .build();

    private final Locale spain = ISOCountryCode.builder()
            .identifier("ES")
            .name("Spain")
            .build();

    private final AddressProperties homeAddress =
            AddressProperties.builder()
                    .use(Set.of("home"))
                    .address(GeographicAddress.builder()
                            .country(spain)
                            .addressLine(List.of("C/ Valencia 138"))
                            .regionOrState("Barcelona")
                            .city("Barcelona")
                            .zipOrPostCode("08018")
                            .build())
                    .build();

    private final AddressProperties workAddress =
            AddressProperties.builder()
                    .use(Set.of("work"))
                    .address(GeographicAddress.builder()
                            .country(spain)
                            .addressLine(List.of("C/ Pujades 54"))
                            .regionOrState("Barcelona")
                            .city("Barcelona")
                            .zipOrPostCode("08018")
                            .build())
                    .build();

    @BeforeEach
    void setUp() {
        personRepository = new DomainPersonRepository(jpaPersonRepository, jpaPartyRepository, partyRoleTypeRepository,
                partyMapper);
    }

    @AfterEach
    void tearDown() {
        entityManager.flush();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(jpaPersonRepository).isNotNull();
        assertThat(jpaAddressRepository).isNotNull();
        assertThat(addressMapper).isNotNull();
        assertThat(jpaPartyRepository).isNotNull();
        assertThat(jpaAuthenticationRepository).isNotNull();
        assertThat(partyRoleTypeRepository).isNotNull();
    }

    @Test
    void shouldSavePerson() {

        final var created = personRepository.save(someGuy);
        assertThat(created).usingRecursiveComparison().ignoringFields("identifier").isEqualTo(someGuy);
        assertThat(created.getIdentifier()).isNotNull();
    }

    @Test
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldSavePersonAndAddress() {

        someGuy.getAddressProperties().add(personalEmail);
        final var created = personRepository.save(someGuy);
        assertThat(created).usingRecursiveComparison().ignoringFields("identifier").isEqualTo(someGuy);
        assertThat(created.getIdentifier()).isNotNull();
        assertThat(created.getIdentifier().getId()).isNotNull();
    }

    @Test
    void shouldModifyAddress() {

        someGuy.getAddressProperties().add(personalEmail);
        someGuy.getAddressProperties().add(homeAddress);
        someGuy.getAddressProperties().add(workAddress);
        final var created = personRepository.save(someGuy);
        entityManager.flush();
        final var oldHomeAddress = created.getAddressProperties().stream()
                .filter(p -> p.getUse().contains("home")).findFirst().orElseThrow();
        final var newHomeAddress = oldHomeAddress.toBuilder()
                .address(GeographicAddress.builder()
                        .addressLine(List.of("C/ Pere IV 121"))
                        .regionOrState("Barcelona")
                        .city("Barcelona")
                        .zipOrPostCode("08018")
                        .country(spain)
                        .build())
                .build();
        created.getAddressProperties().removeIf(p -> p.getUse().contains("home"));
        created.getAddressProperties().add(newHomeAddress);
        final var modified = personRepository.save(created);
        entityManager.flush();
        final var actual = personRepository.findByIdentifier(created.getIdentifier().getId()).orElseThrow();
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(modified);
    }

    @Test
    void shouldFindPersonById() {

        someGuy.getAddressProperties().add(personalEmail);
        final var created = personRepository.save(someGuy);
        final var actual = personRepository.findByIdentifier(created.getIdentifier().getId()).orElseThrow();
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(created);
    }

    /*@Test
    void shouldRemoveAddress() {

        someGuy.getAddressProperties().add(personalEmail);
        someGuy.getAddressProperties().add(homeAddress);
        final var created = personRepository.create(someGuy);
        assertThat(created).usingRecursiveComparison().ignoringFields("partyIdentifier").isEqualTo(someGuy);
        final var homeAddress = created.getAddressProperties().stream().filter(p -> p.getUse().contains("home"))
                .findFirst().orElseThrow();
        created.getAddressProperties().removeIf(p -> p.equals(homeAddress));
        personRepository.removeAddress(created.getPartyIdentifier().getId(),
                addressProperties -> addressProperties.getUse().contains("home"));
        final var actual = personRepository.findByIdentifier(created.getPartyIdentifier().getId()).orElseThrow();
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(created);
    }*/

    @Test
    void shouldAddAddress() {

        someGuy.getAddressProperties().add(personalEmail);
        someGuy.getAddressProperties().add(workAddress);
        final var created = personRepository.save(someGuy);
        entityManager.flush();
        created.getAddressProperties().add(homeAddress);
        final var modified = personRepository.save(created);
        entityManager.flush();
        final var actual = personRepository.findByIdentifier(created.getIdentifier().getId()).orElseThrow();
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(modified);
    }

    @Test
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldFindPersonByIdWithEntityGraph() {

        someGuy.getAddressProperties().add(personalEmail);
        final var created = personRepository.save(someGuy);
        entityManager.flush();
        final var actual = personRepository.findByIdentifier(created.getIdentifier().getId()).orElseThrow();
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(created);
//        jpaPersonRepository.deleteAll();
    }

    @Test
    void shouldCreateAuthentication() {
        someGuy.getAuthentications().add(EmailPasswordAuthentication.builder()
                .emailAddress(personalEmail.getAddress().getAddress())
                .password("password")
                .build());
//        someGuy.getAddressProperties().add(personalEmail);
        final var created = personRepository.save(someGuy);
        entityManager.flush();
        assertThat(created).usingRecursiveComparison().ignoringFields("identifier").isEqualTo(someGuy);
        assertThat(created.getIdentifier()).isNotNull();
        assertThat(created.getIdentifier().getId()).isNotNull();
    }

    @Test
    void shouldCreateRole() {
        final var developer = new JpaPartyRoleType();
        developer.setName("Developer");
        developer.setDescription("Java Developer");
        partyRoleTypeRepository.save(developer);
        entityManager.flush();
        someGuy.getRoles()
                .add(PartyRole.builder()
                        .type(PartyRoleType.builder().name(developer.getName())
                                .description(developer.getDescription()).build())
                        .build());
        final var created = personRepository.save(someGuy);
        assertThat(created).usingRecursiveComparison()
                .ignoringFields("identifier", "roles.identifier").isEqualTo(someGuy);
        assertThat(created.getIdentifier()).isNotNull();
        assertThat(created.getIdentifier().getId()).isNotNull();
    }

    @Test
    void shouldChangeName() {
        someGuy.getAddressProperties().add(personalEmail);
        someGuy.getAddressProperties().add(homeAddress);
        someGuy.getAddressProperties().add(workAddress);
        final var developer = new JpaPartyRoleType();
        developer.setName("Developer");
        developer.setDescription("Java Developer");
        partyRoleTypeRepository.save(developer);
        entityManager.flush();
        someGuy.getRoles()
                .add(PartyRole.builder()
                        .type(PartyRoleType.builder().name(developer.getName())
                                .description(developer.getDescription()).build())
                        .build());
        someGuy.getAuthentications().add(EmailPasswordAuthentication.builder()
                .emailAddress(personalEmail.getAddress().getAddress())
                .password("password")
                .build());
        someGuy.getAuthentications().add(OidcAuthentication.builder()
                .username(personalEmail.getAddress().getAddress())
                .provider("google")
                .build());
        final var created = (Person) personRepository.save(someGuy);
        entityManager.flush();
        final Person modified = created.toBuilder().personName(created.getPersonName().toBuilder()
                .familyName("Rodriguez Liccioni").build()).build();
        final var actual = personRepository.save(modified);
        entityManager.flush();
        assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(modified);
    }
}