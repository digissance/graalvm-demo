package biz.digissance.graalvmdemo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class MyOAuth2AuthorizationRequestAuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH_COOKIE_NAME = "OAUTH";
    private final ObjectMapper mapper;

    public MyOAuth2AuthorizationRequestAuthorizationRequestRepository(final ObjectMapper objectMapper) {
        this.mapper = objectMapper;
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(final HttpServletRequest request) {
        return null;
    }

    @Override
    public void saveAuthorizationRequest(
            final OAuth2AuthorizationRequest authorizationRequest,
            final HttpServletRequest request,
            final HttpServletResponse response) {

        Cookie cookie = new Cookie(OAUTH_COOKIE_NAME, serializeCookie(authorizationRequest));
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setMaxAge(60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @SneakyThrows
    private String serializeCookie(final OAuth2AuthorizationRequest authorizationRequest) {
        final var toCookie = new MyOAuth2AuthorizationRequest();
        toCookie.setAuthorizationUri(authorizationRequest.getAuthorizationUri());
        toCookie.setClientId(authorizationRequest.getClientId());
        toCookie.setRedirectUri(authorizationRequest.getRedirectUri());
        toCookie.setScopes(new ArrayList<>(authorizationRequest.getScopes()));
        toCookie.setState(authorizationRequest.getState());
        toCookie.setAdditionalParameters(authorizationRequest.getAdditionalParameters());
        toCookie.setAttributes(authorizationRequest.getAttributes());
        toCookie.setAuthorizationRequestUri(authorizationRequest.getAuthorizationRequestUri());
        return encodeBase64(mapper.writeValueAsBytes(toCookie));
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            final HttpServletRequest request, final HttpServletResponse response) {
        final var oAuth2AuthorizationRequest = Optional.ofNullable(request.getCookies())
                .map(Arrays::stream)
                .flatMap(p -> p
                        .filter(q -> q.getName().equalsIgnoreCase(OAUTH_COOKIE_NAME))
                        .findFirst())
                .map(this::deserializeCookie)
                .orElse(null);
        Cookie cookie = new Cookie(OAUTH_COOKIE_NAME, "");
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return oAuth2AuthorizationRequest;
    }

    @SneakyThrows
    private OAuth2AuthorizationRequest deserializeCookie(final Cookie cookie) {
        final var fromCookie = mapper.readValue(decodeBase64(cookie.getValue()), MyOAuth2AuthorizationRequest.class);
        return OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri(fromCookie.getAuthorizationUri())
                .clientId(fromCookie.getClientId())
                .redirectUri(fromCookie.getRedirectUri())
                .scopes(new HashSet<>(fromCookie.getScopes()))
                .state(fromCookie.getState())
                .additionalParameters(fromCookie.getAdditionalParameters())
                .attributes(fromCookie.getAttributes())
                .authorizationRequestUri(fromCookie.getAuthorizationRequestUri())
                .build();
    }

    private String encodeBase64(final byte[] cookie) {
        return Base64.getEncoder().encodeToString(cookie);
    }

    private byte[] decodeBase64(final String cookie) {
        return Base64.getDecoder().decode(cookie);
    }
}
