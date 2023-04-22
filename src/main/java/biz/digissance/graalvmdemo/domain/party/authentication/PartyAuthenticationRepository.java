package biz.digissance.graalvmdemo.domain.party.authentication;

import java.util.List;
import java.util.Optional;

public interface PartyAuthenticationRepository {

    Optional<EmailPasswordAuthenticationProjection> findPasswordAuthByUsername(String username);
    List<EmailPasswordAuthenticationProjection> findAuthByUsername(String username);
}
