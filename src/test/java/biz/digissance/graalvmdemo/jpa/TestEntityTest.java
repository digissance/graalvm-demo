package biz.digissance.graalvmdemo.jpa;

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
        PersonEntity testEntity = PersonEntity.builder().build();
        Set<PersonEntity> set = new HashSet<>();

        set.add(testEntity);
        jpaPersonRepository.save(testEntity);

        Assert.isTrue(set.contains(testEntity), "Entity not found in the set");
    }
}