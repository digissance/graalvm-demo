package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.domain.party.authentication.DomainEmailPasswordPartyAuthenticationRepository;
import biz.digissance.graalvmdemo.domain.party.authentication.EmailPasswordPartyAuthenticationRepository;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    @Value("${SECURITY_DEBUG_ENABLED:false}")
    private boolean securityDebugEnable;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring()
//                .requestMatchers("/actuator/**", "/css/**", "/images/**", "/js/**", "/webfonts/**")
                .and().debug(securityDebugEnable);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(final UserDetailsService userDetailsService,
                                                         final PasswordEncoder passwordEncoder) {
        final var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,
                                                   final AuthenticationProvider daoAuthProvider,
                                                   final UserDetailsService userDetailsService) throws Exception {
        return http
                .authenticationProvider(daoAuthProvider)
                .authorizeHttpRequests(p -> {
                    p.requestMatchers("/", "/login", "/register/**", "/error",
                            "/actuator/**", "/css/**", "/images/**", "/js/**", "/webfonts/**").permitAll();
                    p.anyRequest().authenticated();
                })
                .csrf().disable()
                .rememberMe(rem -> {
                    rem.alwaysRemember(true);
                    rem.userDetailsService(userDetailsService);
                })
//                .headers().frameOptions().disable().and()
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
//                                .permitAll()
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .anonymous(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public EmailPasswordPartyAuthenticationRepository emailPasswordPartyAuthenticationRepository(
            final JpaPartyAuthenticationRepository repository,
            final PartyMapper mapper) {
        return new DomainEmailPasswordPartyAuthenticationRepository(repository, mapper);
    }

    @Bean
    public UserDetailsService userDetailsService(final JpaPartyAuthenticationRepository authRepository,
                                                 final EmailPasswordPartyAuthenticationRepository repository) {
        return new EmailPasswordUserDetailsService(authRepository, repository);
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        final var defaultMethodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        defaultMethodSecurityExpressionHandler.setPermissionEvaluator(new PermissionEvaluator() {
            @Override
            public boolean hasPermission(final Authentication authentication, final Object targetDomainObject,
                                         final Object permission) {
                return true;
            }

            @Override
            public boolean hasPermission(final Authentication authentication, final Serializable targetId,
                                         final String targetType,
                                         final Object permission) {
                return false;
            }
        });
        return defaultMethodSecurityExpressionHandler;
    }
}
