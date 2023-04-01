package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.jpa.session.Session;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Slf4j
public class DelegatingPersistentTokenRepository implements PersistentTokenRepository {
    private final SessionService sessionService;

    public DelegatingPersistentTokenRepository(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        final var sessionId = token.getSeries();
        final var userId = token.getUsername();
        sessionService.createSession(sessionId, userId, token.getTokenValue());
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        try {
            sessionService.useSession(series, tokenValue, lastUsed.toInstant());
        } catch (NoSuchSessionException e) {
            log.warn("Session {} doesn't exists.", series);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String sessionId) {
        return sessionService
                .findSession(sessionId)
                .map(this::toPersistentRememberMeToken)
                .orElse(null);
    }

    @Override
    public void removeUserTokens(String username) {
        sessionService.logoutUser(username);
    }

    private PersistentRememberMeToken toPersistentRememberMeToken(Session session) {
        Instant lastUsedAt =
                Optional.ofNullable(session.getLastUsedAt()).orElseGet(session::getIssuedAt);
        return new PersistentRememberMeToken(session.getUserId(), session.getSessionId(), session.getToken(),
                Date.from(lastUsedAt));
    }

}
