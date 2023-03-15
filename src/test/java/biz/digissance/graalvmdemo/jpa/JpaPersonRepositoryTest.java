package biz.digissance.graalvmdemo.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import biz.digissance.graalvmdemo.domain.DomainPersonRepository;
import biz.digissance.graalvmdemo.domain.PersonRepository;
import jakarta.persistence.EntityManager;
import javax.sql.DataSource;
import net.liccioni.archetypes.party.Person;
import net.liccioni.archetypes.party.PersonName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class JpaPersonRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JpaPersonRepository jpaPersonRepository;

    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(jpaPersonRepository).isNotNull();
        assertThat(personMapper).isNotNull();
    }

    @Test
    void shouldSavePerson() {

        final var gus = Person.builder()
                .personName(PersonName.builder().givenName("Gustavo")
                        .familyName("Rodriguez")
                        .build())
                .build();
        final PersonRepository personRepository = new DomainPersonRepository(jpaPersonRepository, personMapper);
        final var created = personRepository.save(gus);
        assertThat(created).usingRecursiveComparison().ignoringFields("partyIdentifier").isEqualTo(gus);
        assertThat(created.getPartyIdentifier()).isNotNull();
    }
}