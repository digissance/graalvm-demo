package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.domain.party.PartyService;
import biz.digissance.graalvmdemo.domain.party.authentication.DomainPartyAuthenticationRepository;
import biz.digissance.graalvmdemo.domain.party.authentication.PartyAuthenticationRepository;
import biz.digissance.graalvmdemo.http.OidcRegisterRequest;
import biz.digissance.graalvmdemo.jpa.party.PartyMapper;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import java.io.Serializable;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
//                .requestMatchers("/actuator/**", "/css/**", "/images/**", "/js/**", "/webfonts/**")
                .and().debug(securityDebugEnable);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*@Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

    @Bean
    public UserCache userCache(final CacheManager cacheManager) throws Exception {

        Cache cache = (Cache) cacheManager.getCache("userCache");
        return new SpringCacheBasedUserCache(cache);
    }*/

    @Bean
    public UserDetailsService userDetailsService(final PartyAuthenticationRepository repository) {
//        final var cachingUserDetailsService =
//                new CachingUserDetailsService(new EmailPasswordUserDetailsService(repository));
//        cachingUserDetailsService.setUserCache(userCache);
//        return cachingUserDetailsService;
        return new EmailPasswordUserDetailsService(repository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(final UserDetailsService userDetailsService,
                                                         final PasswordEncoder passwordEncoder) {
        final var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        daoAuthenticationProvider.setUserCache(
//                new SpringCacheBasedUserCache(cacheManager.getCache("userDaoCache")));
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,
                                                   final AuthenticationProvider daoAuthProvider,
                                                   final UserDetailsService userDetailsService,
                                                   final PartyService partyService,
                                                   final PersistentTokenRepository persistentTokenRepository,
                                                   final PartyAuthenticationRepository partyAuthRepository)
            throws Exception {
//        http.getSharedObject(AuthenticationManagerBuilder.class).eraseCredentials(false);
        return http
                /*.securityContext(sec->sec.securityContextRepository(new SecurityContextRepository() {
                    @Override
                    public SecurityContext loadContext(final HttpRequestResponseHolder requestResponseHolder) {
                        return null;
                    }

                    @Override
                    public void saveContext(final SecurityContext context, final HttpServletRequest request,
                                            final HttpServletResponse response) {

                    }

                    @Override
                    public boolean containsContext(final HttpServletRequest request) {
                        return false;
                    }
                }))*/
                .authenticationProvider(daoAuthProvider)
                .authorizeHttpRequests(p -> {
                    p.requestMatchers(
                            "/", "/login/**", "/register/**", "/error", "/oauth2/**",
                            "/actuator/**", "/css/**", "/images/**", "/js/**", "/webfonts/**").permitAll();
                    p.anyRequest().authenticated();
                })
                .csrf().disable()
                .rememberMe(rem -> {
                    /*final var cache = cacheManager.getCache("rememberMeUserCache");
                    final var allPurposeService = new CachingUserDetailsService(new AllPurposeUserDetailsService
                    (partyAuthRepository,"mykey"));
                    allPurposeService.setUserCache(new SpringCacheBasedUserCache(cache));
                    */
                    rem.alwaysRemember(true);
                    rem.tokenRepository(persistentTokenRepository);
//                    final var myRememberMeServices = new MyRememberMeServices("notUsed", userDetailsService, persistentTokenRepository);
//                    myRememberMeServices.setAlwaysRemember(true);
//                    rem.rememberMeServices(myRememberMeServices);
//                    rem.userDetailsService(allPurposeService);
                })
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
//                                .permitAll()
                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(p -> {
                    p.loginPage("/login");
                    p.userInfoEndpoint(j -> {
                        final var oidcUserService = new OidcUserService();
                        j.oidcUserService(new OAuth2UserService<OidcUserRequest, OidcUser>() {
                            @Override
                            public OidcUser loadUser(final OidcUserRequest userRequest)
                                    throws OAuth2AuthenticationException {
                                final var oidcUser = oidcUserService.loadUser(userRequest);
                                partyService.register(OidcRegisterRequest.builder()
                                        .email(oidcUser.getEmail())
                                        .firstName(oidcUser.getGivenName())
                                        .lastName(oidcUser.getFamilyName())
                                        .provider(userRequest.getClientRegistration().getClientName())
                                        .build());
                                return oidcUser;
                            }
                        });
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
