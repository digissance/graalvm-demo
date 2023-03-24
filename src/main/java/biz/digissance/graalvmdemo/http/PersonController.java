package biz.digissance.graalvmdemo.http;

import biz.digissance.graalvmdemo.domain.party.PersonRepository;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.liccioni.archetypes.party.Person;
import net.liccioni.archetypes.party.PersonName;
import org.springframework.web.bind.annotation.GetMapping;
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
    public Person createTestEntities(@Valid @RequestBody PersonDTO person) {
        log.info("Request received: " + person);
        var p = repository.create(Person.builder()
                .personName(PersonName.builder()
                        .givenName(person.getFirstName())
                        .familyName(person.getLastName())
                        .build())
                .build());
        log.info(p.toString());
        return p;
    }

    @GetMapping
    public List<Person> getPersons() {
        return repository.findAll();
    }
//
//    @GetMapping("/{identifier}")
//    public PersonEntity getPerson(@PathVariable String identifier) {
//        return repository.findByIdentifier(identifier).orElseThrow();
//    }
}
