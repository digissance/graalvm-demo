package biz.digissance.graalvmdemo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final ObjectMapper mapper;
    private final String SECURITY_CONTEXT_COOKIE_NAME;

    public MySecurityContextRepository(final ObjectMapper objectMapper, final String SECURITY_CONTEXT_COOKIE_NAME) {
        this.SECURITY_CONTEXT_COOKIE_NAME = SECURITY_CONTEXT_COOKIE_NAME;
        this.mapper = objectMapper;
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
        final var p = mapper.readValue(Base64.getDecoder().decode(cookie.getValue()), Map.class);
        final List<GrantedAuthority> roles =
                ((List<String>) p.getOrDefault("roles", new ArrayList<>())).stream().map(
                        SimpleGrantedAuthority::new).collect(Collectors.toList());
        final var auth =
                new PreAuthenticatedAuthenticationToken(p.get("sub"), null, roles);
        return new SecurityContextImpl(auth);
    }

    @Override
    @SneakyThrows
    public void saveContext(final SecurityContext context, final HttpServletRequest request,
                            final HttpServletResponse response) {
        final String secContext;
        final var roles = context.getAuthentication()
                .getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        final var sec = Map.of("sub", context.getAuthentication().getName(), "roles", roles);
        secContext = Base64.getEncoder().encodeToString(mapper.writeValueAsBytes(sec));
        Cookie cookie = new Cookie(SECURITY_CONTEXT_COOKIE_NAME, secContext);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public boolean containsContext(final HttpServletRequest request) {
        return Arrays.stream(request.getCookies()).anyMatch(p -> p.getName().equals(SECURITY_CONTEXT_COOKIE_NAME));
    }
}
