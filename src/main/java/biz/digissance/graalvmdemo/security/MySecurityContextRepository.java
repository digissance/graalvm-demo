package biz.digissance.graalvmdemo.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

class MySecurityContextRepository implements SecurityContextRepository {
    private final String SECURITY_CONTEXT_COOKIE_NAME;

    public MySecurityContextRepository(final String SECURITY_CONTEXT_COOKIE_NAME) {
        this.SECURITY_CONTEXT_COOKIE_NAME = SECURITY_CONTEXT_COOKIE_NAME;
    }

    @Override
    public SecurityContext loadContext(final HttpRequestResponseHolder requestResponseHolder) {

        return Optional.ofNullable(requestResponseHolder.getRequest().getCookies()).stream()
                .flatMap(Arrays::stream)
                .filter(p -> p.getName().equals(SECURITY_CONTEXT_COOKIE_NAME))
                .findFirst()
                .map(this::deSerializeCookie)
                .orElseGet(SecurityContextImpl::new);
    }

    @SneakyThrows
    private SecurityContext deSerializeCookie(final Cookie cookie) {

        ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(cookie.getValue().getBytes()));
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            final Map<String, Object> p = (Map<String, Object>) ois.readObject();
            final List<GrantedAuthority> roles =
                    ((List<String>) p.getOrDefault("roles", new ArrayList<>())).stream().map(
                            SimpleGrantedAuthority::new).collect(Collectors.toList());
            final var auth =
                    new PreAuthenticatedAuthenticationToken(p.get("sub"), null, roles);
            return new SecurityContextImpl(auth);
        }
    }

    @Override
    @SneakyThrows
    public void saveContext(final SecurityContext context, final HttpServletRequest request,
                            final HttpServletResponse response) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            final String secContext;
            final var roles = context.getAuthentication()
                    .getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            final var sec = Map.of("sub", context.getAuthentication().getName(), "roles", roles);
            ObjectOutputStream out = null;
            out = new ObjectOutputStream(bos);
            out.writeObject(sec);
            out.flush();
            byte[] yourBytes = bos.toByteArray();
            secContext = Base64.getEncoder().encodeToString(yourBytes);
            Cookie cookie = new Cookie(SECURITY_CONTEXT_COOKIE_NAME, secContext);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    @Override
    public boolean containsContext(final HttpServletRequest request) {
        return Optional.of(request).map(HttpServletRequest::getCookies)
                .stream()
                .flatMap(Arrays::stream)
                .map(Cookie::getName)
                .anyMatch(SECURITY_CONTEXT_COOKIE_NAME::equals);
    }
}
