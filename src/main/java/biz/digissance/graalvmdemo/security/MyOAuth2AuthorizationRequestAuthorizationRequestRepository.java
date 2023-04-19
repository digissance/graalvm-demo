package biz.digissance.graalvmdemo.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class MyOAuth2AuthorizationRequestAuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH_COOKIE_NAME = "OAUTH";

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
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setMaxAge(60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @SneakyThrows
    private String serializeCookie(final OAuth2AuthorizationRequest authorizationRequest) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ObjectOutputStream out = null;
            out = new ObjectOutputStream(bos);
            out.writeObject(authorizationRequest);
            out.flush();
            byte[] yourBytes = bos.toByteArray();
            return Base64.getEncoder().encodeToString(yourBytes);
        }
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

        ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(cookie.getValue().getBytes()));
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (OAuth2AuthorizationRequest) ois.readObject();
        }
    }
}
