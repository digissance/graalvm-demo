package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.domain.party.DomainPartyRepository;
import biz.digissance.graalvmdemo.domain.party.PartyService;
import biz.digissance.graalvmdemo.domain.party.PartyServiceImpl;
import biz.digissance.graalvmdemo.domain.party.PartyRepository;
import biz.digissance.graalvmdemo.jpa.party.JpaPartyRepository;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleTypeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
    public PartyRepository personRepository(
            final JpaPersonRepository jpaPersonRepository,
            final PartyMapper partyMapper,
            final JpaPartyRepository partyRepository,
            final JpaPartyRoleTypeRepository partyRoleTypeRepository) {
        return new DomainPartyRepository(jpaPersonRepository, partyRepository, partyRoleTypeRepository, partyMapper);
    }

    @Bean
    public PartyService partyService(final PartyRepository partyRepository,
                                     final PartyMapper partyMapper,
                                     final PasswordEncoder passwordEncoder) {
        return new PartyServiceImpl(partyRepository, partyMapper, passwordEncoder);
    }
}
