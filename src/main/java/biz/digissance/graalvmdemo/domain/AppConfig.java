package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.jpa.JpaPersonRepository;
import biz.digissance.graalvmdemo.jpa.PersonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PersonRepository personRepository(
            final JpaPersonRepository jpaPersonRepository,
            final PersonMapper personMapper) {
        return new DomainPersonRepository(jpaPersonRepository, personMapper);
    }
}
