package biz.digissance.graalvmdemo;

import biz.digissance.graalvmdemo.http.PersonDTO;
import lombok.SneakyThrows;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
//@ImportRuntimeHints(GraalvmDemoApplication.GraalvmDemoApplicationRuntimeHints.class)
public class GraalvmDemoApplication {

    public static void main(String[] args) {
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

//    static class GraalvmDemoApplicationRuntimeHints implements RuntimeHintsRegistrar {
//
//        @SneakyThrows
//        @Override
//        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
//            hints.reflection()
//                    .registerConstructor(PersonDTO.PersonDTOBuilder.class.getDeclaredConstructor(), ExecutableMode.INVOKE)
//                    .registerMethod(PersonDTO.PersonDTOBuilder.class.getMethod("build"), ExecutableMode.INVOKE);
//        }
//    }
}
