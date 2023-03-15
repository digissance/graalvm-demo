package biz.digissance.graalvmdemo;

import biz.digissance.graalvmdemo.jpa.BaseEntityImportBeanDefinitionRegistrar;
import biz.digissance.graalvmdemo.jpa.ApplicationEventPublisherSupplier;
import java.security.Principal;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication(proxyBeanMethods = false)
@ImportRuntimeHints(BaseEntityImportBeanDefinitionRegistrar.class)
public class GraalvmDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraalvmDemoApplication.class, args);
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication).map(Principal::getName);
    }

    @Bean
    public ApplicationEventPublisherSupplier myPublisher(ApplicationEventPublisher publisher) {
        ApplicationEventPublisherSupplier.INSTANCE.setPublisher(publisher);
        return ApplicationEventPublisherSupplier.INSTANCE;
    }
}
