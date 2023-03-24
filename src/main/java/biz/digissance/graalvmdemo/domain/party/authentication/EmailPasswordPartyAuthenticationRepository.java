package biz.digissance.graalvmdemo.domain.party.authentication;

import java.util.Optional;

public interface EmailPasswordPartyAuthenticationRepository {
    Optional<EmailPasswordAuthenticationProjection> findByEmailAddress(String username);
}
