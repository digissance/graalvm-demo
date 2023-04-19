package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.domain.party.PartyService;
import biz.digissance.graalvmdemo.domain.party.authentication.DomainPartyAuthenticationRepository;
import biz.digissance.graalvmdemo.domain.party.authentication.PartyAuthenticationRepository;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

//@EnableCaching
@EnableWebSecurity
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    @Value("${SECURITY_DEBUG_ENABLED:false}")
    private boolean securityDebugEnable;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring()
//                .requestMatchers(PathRequest.toH2Console())
//                .requestMatchers("/actuator/**", "/css/**", "/images/**", "/js/**", "/webfonts/**")
                .and().debug(securityDebugEnable);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(final PartyAuthenticationRepository repository) {
        return new EmailPasswordUserDetailsService(repository);
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
                                                   final UserDetailsService userDetailsService,
                                                   final PartyService partyService,
                                                   final PersistentTokenRepository persistentTokenRepository,
                                                   final PartyAuthenticationRepository partyAuthRepository,
                                                   final ObjectMapper objectMapper)
            throws Exception {
        return http
                .securityContext(sec -> {
//                    sec.securityContextRepository(new RequestAttributeSecurityContextRepository());
                })
//                .securityContext(sec -> sec.securityContextRepository(new SecurityContextRepository() {
//                    private final ObjectMapper mapper = objectMapper;
//
//                    @Override
//                    public SecurityContext loadContext(final HttpRequestResponseHolder requestResponseHolder) {
//
//                        return Optional.ofNullable(requestResponseHolder.getRequest().getCookies()).stream()
//                                .flatMap(Arrays::stream)
//                                .filter(p -> p.getName().equals("J_SEC"))
//                                .findFirst()
//                                .map(p -> {
//                                    try {
//                                        return mapper.readValue(Base64.getDecoder().decode(p.getValue()),
//                                                SecurityContextImpl.class);
//                                    } catch (IOException e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                })
//                                .orElseGet(SecurityContextImpl::new);
//                    }
//
//                    @Override
//                    public void saveContext(final SecurityContext context, final HttpServletRequest request,
//                                            final HttpServletResponse response) {
//                        final String secContext;
//                        try {
//                            secContext = Base64.getEncoder().encodeToString(mapper.writeValueAsBytes(context));
//                            Cookie cookie = new Cookie("J_SEC", secContext);
//                            response.addCookie(cookie);
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public boolean containsContext(final HttpServletRequest request) {
//                        return Arrays.stream(request.getCookies()).anyMatch(p -> p.getName().equals("JSEC"));
//                    }
//                }))
                .authenticationProvider(daoAuthProvider)
                .authorizeHttpRequests(p -> {
                    p.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
                    p.requestMatchers(
                            "/", "/login/**", "/register/**", "/error", "/oauth2/**",
                            "/actuator/**", "/css/**", "/images/**", "/js/**", "/webfonts/**").permitAll();
                    p.anyRequest().authenticated();
                })
                .csrf().disable()
                .rememberMe(rem -> {
                    rem.alwaysRemember(true);
                    rem.tokenRepository(persistentTokenRepository);
                    rem.userDetailsService(new AllPurposeUserDetailsService(partyAuthRepository,"123456"));
                })
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
//                                .permitAll()
                )
                .sessionManagement(session -> {
//                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .oauth2Login(configurer -> {
                    configurer.loginPage("/login");
                    configurer.authorizationEndpoint(p -> p.authorizationRequestRepository(
                            new MyOAuth2AuthorizationRequestAuthorizationRequestRepository(objectMapper)));
                    configurer.userInfoEndpoint(p -> {

                        p.oidcUserService(
                                new MyOidcUserRequestOidcUserOAuth2UserService(new OidcUserService(), partyService));
                    });
                })
                .build();
    }

    @Bean
    public PartyAuthenticationRepository emailPasswordPartyAuthenticationRepository(
            final JpaPartyAuthenticationRepository repository,
            final PartyMapper mapper) {
        return new DomainPartyAuthenticationRepository(repository, mapper);
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

    @Bean
    public PersistentTokenRepository delegatingPersistentTokenRepository(final DataSource dataSource) {
        final var jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
