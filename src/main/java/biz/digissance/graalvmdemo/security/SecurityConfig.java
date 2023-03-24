package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.domain.DomainPartyRepository;
import biz.digissance.graalvmdemo.domain.PartyRepository;
import biz.digissance.graalvmdemo.jpa.party.JpaPartyRepository;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import java.io.Serializable;
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring().requestMatchers("/css/**", "/images/**", "/js/**", "/webfonts/**")
                .and().debug(true);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        final var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,
                                                   final AuthenticationProvider daoAuthProvider,
                                                   final UserDetailsService userDetailsService) throws Exception {
//        http
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/", "/home").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .permitAll()
//                )
//                .logout((logout) -> logout.permitAll());

//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .anonymous().disable()
//                .httpBasic()
//                .and()
//                .sessionManagement().disable()
//                .securityContext().disable()//.securityContextRepository(new NullSecurityContextRepository()).and()
//                .requestCache().disable()
//                .logout().disable()
//                .exceptionHandling().disable()
//                .headers().disable()
//                .sessionManagement(p -> p.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests().anyRequest().permitAll().and()
//                .build();
        return http
                .authenticationProvider(daoAuthProvider)
                .authorizeHttpRequests(p -> {
                    p.requestMatchers("/", "/login", "/register/**").permitAll();
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
                .anonymous(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PartyRepository partyAuthenticationRepository(
            final JpaPartyRepository repository,
            final PartyMapper mapper,
            final JpaPartyAuthenticationRepository authRepository) {
        return new DomainPartyRepository(repository, authRepository, mapper);
    }

    @Bean
    public UserDetailsService userDetailsService(
            final PartyRepository repository,
            final JpaPartyRepository jpaRepository,
            final JpaPartyAuthenticationRepository authRepository) {
        return new EmailPasswordUserDetailsService(repository, jpaRepository, authRepository);
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
