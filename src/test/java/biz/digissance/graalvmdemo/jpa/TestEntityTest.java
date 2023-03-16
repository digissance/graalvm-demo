package biz.digissance.graalvmdemo.jpa;

import biz.digissance.graalvmdemo.jpa.party.person.JpaPerson;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

@SpringBootTest
@ActiveProfiles("test")
class TestEntityTest {

    @Autowired
    private JpaPersonRepository jpaPersonRepository;

    @Test
    void shouldFindEntity() {
//        JpaPerson testEntity = JpaPerson.builder().build();
//        Set<JpaPerson> set = new HashSet<>();

//        set.add(testEntity);
//        jpaPersonRepository.save(testEntity);

//        Assert.isTrue(set.contains(testEntity), "Entity not found in the set");
    }
}