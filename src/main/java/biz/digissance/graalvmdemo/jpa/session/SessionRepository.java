package biz.digissance.graalvmdemo.jpa.session;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByUserId(String userId);

    Optional<Session> findBySessionId(String id);
}
