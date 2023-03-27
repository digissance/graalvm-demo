package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.domain.party.DomainPersonRepository;
import biz.digissance.graalvmdemo.domain.party.PartyService;
import biz.digissance.graalvmdemo.domain.party.PartyServiceImpl;
import biz.digissance.graalvmdemo.domain.party.PersonRepository;
import biz.digissance.graalvmdemo.jpa.party.JpaPartyRepository;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public PersonRepository personRepository(
            final JpaPersonRepository jpaPersonRepository,
            final PartyMapper partyMapper,
            final JpaPartyRepository partyRepository) {
        return new DomainPersonRepository(jpaPersonRepository, partyRepository, partyMapper);
    }

    @Bean
    public PartyService partyService(final PersonRepository personRepository,
                                     final PartyMapper partyMapper,
                                     final PasswordEncoder passwordEncoder){
        return new PartyServiceImpl(personRepository, partyMapper, passwordEncoder);
    }
}
