package biz.digissance.graalvmdemo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Slf4j
public class MyRememberMeServices extends TokenBasedRememberMeServices {

    private static final RememberMeTokenAlgorithm DEFAULT_ENCODING_ALGORITHM = RememberMeTokenAlgorithm.SHA256;

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
    }
}
