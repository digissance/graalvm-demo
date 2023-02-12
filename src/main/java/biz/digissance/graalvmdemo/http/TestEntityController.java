package biz.digissance.graalvmdemo.http;

import biz.digissance.graalvmdemo.jpa.TestEntity;
import biz.digissance.graalvmdemo.jpa.TestEntityRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testEntities")
public class TestEntityController {

//    private final TestEntityRepository repository;
//
//    public TestEntityController(final TestEntityRepository repository) {
//        this.repository = repository;
//    }
//
//    @GetMapping
//    public List<TestEntity> getTestEntities() {
//        return repository.findAll();
//    }

//    @PostMapping
//    public TestEntity createTestEntities(@Valid @RequestBody TestEntity testEntity) {
//        return repository.save(testEntity);
//    }
}
