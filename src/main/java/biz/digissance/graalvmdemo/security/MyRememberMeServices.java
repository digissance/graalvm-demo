package biz.digissance.graalvmdemo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

@Slf4j
public class MyRememberMeServices extends PersistentTokenBasedRememberMeServices {
    private final PersistentTokenRepository tokenRepository;

    public MyRememberMeServices(final String key,
                                final UserDetailsService userDetailsService,
                                final PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected UserDetails processAutoLoginCookie(final String[] cookieTokens, final HttpServletRequest request,
                                                 final HttpServletResponse response) {
        {
            if (cookieTokens.length != 2) {
                throw new InvalidCookieException("Cookie token did not contain " + 2 + " tokens, but contained '"
                        + Arrays.asList(cookieTokens) + "'");
            }
            String presentedSeries = cookieTokens[0];
            String presentedToken = cookieTokens[1];
            PersistentRememberMeToken token = this.tokenRepository.getTokenForSeries(presentedSeries);
            if (token == null) {
                // No series match, so we can't authenticate using this cookie
                throw new RememberMeAuthenticationException(
                        "No persistent token found for series id: " + presentedSeries);
            }
            // We have a match for this user/series combination
            if (!presentedToken.equals(token.getTokenValue())) {
                // Token doesn't match series value. Delete all logins for this user and throw
                // an exception to warn them.
                this.tokenRepository.removeUserTokens(token.getUsername());
                throw new CookieTheftException(this.messages.getMessage(
                        "PersistentTokenBasedRememberMeServices.cookieStolen",
                        "Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."));
            }
            if (token.getDate().getTime() + getTokenValiditySeconds() * 1000L < System.currentTimeMillis()) {
                throw new RememberMeAuthenticationException("Remember-me login has expired");
            }
            // Token also matches, so login is valid. Update the token value, keeping the
            // *same* series number.
            this.logger.debug(LogMessage.format("Refreshing persistent login token for user '%s', series '%s'",
                    token.getUsername(), token.getSeries()));

            return getUserDetailsService().loadUserByUsername(token.getUsername());
        }
    }

    /*private static final RememberMeTokenAlgorithm DEFAULT_ENCODING_ALGORITHM = RememberMeTokenAlgorithm.SHA256;

    protected MyRememberMeServices(final String key,
                                   final UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    public void onLoginSuccess(final HttpServletRequest request, final HttpServletResponse response,
                               final Authentication successfulAuthentication) {

        final UserDetails userDetails;

        if (successfulAuthentication instanceof UsernamePasswordAuthenticationToken) {
            final var principal = (UserDetails) successfulAuthentication.getPrincipal();
            userDetails = getUserDetailsService().loadUserByUsername(principal.getUsername());
        } else if (successfulAuthentication instanceof OAuth2AuthenticationToken) {
            final var username = ((OAuth2User) successfulAuthentication.getPrincipal()).getAttribute("email");
            userDetails = getUserDetailsService().loadUserByUsername((String) username);
        } else {
            throw new IllegalStateException();
        }
        super.onLoginSuccess(request, response, new UsernamePasswordAuthenticationToken(userDetails, getKey()));
    }*/
}
