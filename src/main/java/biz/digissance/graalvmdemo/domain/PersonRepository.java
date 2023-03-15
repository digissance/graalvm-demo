package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.jpa.PersonEntity;
import java.util.List;
import net.liccioni.archetypes.party.Person;

public interface PersonRepository {
    Person save(Person gus);

    List<Person> findAll();
}
