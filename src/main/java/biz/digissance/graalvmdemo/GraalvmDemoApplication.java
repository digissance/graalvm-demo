package biz.digissance.graalvmdemo;

import biz.digissance.graalvmdemo.jpa.UUIDSequenceIdGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class GraalvmDemoApplication {

    public static void main(String[] args) {
        final var idGenerator = new UUIDSequenceIdGenerator();
        SpringApplication.run(GraalvmDemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @GetMapping("/hello/{var}")
    public String another(@PathVariable final String var) {
        return "hello " + var;
    }

    @PostMapping("/pojo")
    public ResponseEntity<SimplePojo> pojo(@RequestBody final SimplePojo pojo) {
        return ResponseEntity.ok(pojo);
    }
}
