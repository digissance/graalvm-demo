package biz.digissance.graalvmdemo.security;

import static java.util.stream.Collectors.toList;

import biz.digissance.graalvmdemo.jpa.session.Session;
import biz.digissance.graalvmdemo.jpa.session.SessionRepository;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {

        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session createSession(String sessionId, String userId, String token) {
        return createSession(sessionId, userId, token, 0);
    }

    @Override
    public Session createSession(String sessionId, String userId, String token, int minutes) {

        Session session = new Session(sessionId, userId, token, minutes);
        sessionRepository.save(session);

        log.info("Created persistent session {} for user {}.", session.getId(), session.getUserId());

        return session;
    }

    @Override
    public Optional<Session> findSession(String id) {
        Objects.requireNonNull(id);
        return sessionRepository.findBySessionId(id).map(session -> (session.isValid() ? session : null));
    }

    @Override
    public Session getSession(String id) throws NoSuchSessionException {
        return findSession(id).orElseThrow(NoSuchSessionException::new);
    }

    @Override
    public void logoutUser(String userId) {
        List<Long> deletedSessionIds = sessionRepository.findByUserId(userId)
                .stream()
                .peek(session -> session.setDeleted(true))
                .peek(sessionRepository::save)
                .map(Session::getId)
                .collect(toList());

        log.info("Sessions {} of user {} were deleted.", deletedSessionIds, userId);
    }

    @Override
    public void useSession(String sessionId, String value, Instant lastUsedAt)
            throws NoSuchSessionException {

        Session session = getSession(sessionId);
        session.setToken(value);
        session.setLastUsedAt(lastUsedAt);

        sessionRepository.save(session);

        log.info("Auto login with persistent session {} for user {}.", session.getId(), session.getUserId());
    }
}
