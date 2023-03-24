package biz.digissance.graalvmdemo.http;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

//    private final SpringTemplateEngine templateEngine;

//    public MvcConfig(final SpringTemplateEngine templateEngine) {
//        this.templateEngine = templateEngine;
//        this.templateEngine.addDialect(new SpringSecurityDialect());
//    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
    }

//    @Bean
//    public SpringTemplateEngine templateEngine(){
//        final var springTemplateEngine = new SpringTemplateEngine();
//        springTemplateEngine.addDialect(new SpringSecurityDialect());
//        return springTemplateEngine;
//    }
}