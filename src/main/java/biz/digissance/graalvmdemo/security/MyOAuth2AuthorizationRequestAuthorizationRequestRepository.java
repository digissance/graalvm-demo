package biz.digissance.graalvmdemo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;

public class MyOAuth2AuthorizationRequestAuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH_COOKIE_NAME = "OAUTH";
    private final ObjectMapper mapper;

    public MyOAuth2AuthorizationRequestAuthorizationRequestRepository(final ObjectMapper objectMapper) {
        this.mapper = objectMapper;
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OAuth2AuthorizationResponseType.class,
                new OAuth2AuthorizationResponseTypeDeserializer());
        mapper.registerModule(module);
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(
            final HttpServletRequest request) {
        return null;
    }

    @Override
    public void saveAuthorizationRequest(
            final OAuth2AuthorizationRequest authorizationRequest,
            final HttpServletRequest request,
            final HttpServletResponse response) {

        Cookie cookie = new Cookie(OAUTH_COOKIE_NAME, serializeCookie(authorizationRequest));
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @SneakyThrows
    private String serializeCookie(final OAuth2AuthorizationRequest authorizationRequest) {
        return Base64.getEncoder()
                .encodeToString(mapper.writeValueAsBytes(authorizationRequest));
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            final HttpServletRequest request, final HttpServletResponse response) {
        Cookie cookie = new Cookie(OAUTH_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return Optional.ofNullable(request.getCookies())
                .map(Arrays::stream)
                .flatMap(p -> p
                        .filter(q -> q.getName().equalsIgnoreCase(OAUTH_COOKIE_NAME))
                        .findFirst())
                .map(this::deserializeCookie)
                .orElse(null);
    }

    @SneakyThrows
    private OAuth2AuthorizationRequest deserializeCookie(final Cookie cookie) {
        return mapper.readValue(
                Base64.getDecoder().decode(cookie.getValue()),
                OAuth2AuthorizationRequest.class);
    }
}
