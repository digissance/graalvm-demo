package biz.digissance.graalvmdemo.jpa;

import biz.digissance.graalvmdemo.jpa.event.ApplicationEventPublisherSupplier;
import java.security.Principal;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

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
