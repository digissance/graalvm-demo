package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.jpa.session.Session;
import java.time.Instant;
import java.util.Optional;

public interface SessionService {


    Session createSession(String sessionId, String userId, String token);

    Session createSession(String sessionId, String userId, String token, int minutes);

    Optional<Session> findSession(String sessionId);

    Session getSession(String sessionId) throws NoSuchSessionException;

    void logoutUser(String userId);

    void useSession(String sessionId, String value, Instant lastUsedAt) throws NoSuchSessionException;
}
