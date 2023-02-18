package biz.digissance.graalvmdemo.http;

import biz.digissance.graalvmdemo.jpa.JpaPersonName;
import biz.digissance.graalvmdemo.jpa.PersonEntity;
import biz.digissance.graalvmdemo.jpa.PersonRepository;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository repository;

    public PersonController(final PersonRepository repository) {
        this.repository = repository;
    }

    @PostMapping
//    @PreAuthorize("hasPermission(#person,'WRITE')")
    public PersonEntity createTestEntities(@Valid @RequestBody PersonDTO person) {
        log.info("Request received: " + person);
        PersonEntity p = repository.save(PersonEntity.builder()
                .personName(JpaPersonName.builder()
                        .givenName(person.getName())
                        .familyName(person.getLastname())
                        .build())
                .build());
        log.info(p.toString());
        return p;
    }

    @GetMapping
    public List<PersonEntity> getPersons() {
        return repository.findAll();
    }

    @GetMapping("/{identifier}")
    public PersonEntity getPerson(@PathVariable String identifier) {
        return repository.findByIdentifier(identifier).orElseThrow();
    }
}
