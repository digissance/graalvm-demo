package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PersonRepository personRepository(
            final JpaPersonRepository jpaPersonRepository,
            final PartyMapper partyMapper) {
        return new DomainPersonRepository(jpaPersonRepository, partyMapper);
    }
}
