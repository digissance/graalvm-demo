package biz.digissance.graalvmdemo.security;

import java.util.Optional;
import net.liccioni.archetypes.party.PartyAuthentication;

public interface PartyAuthenticationRepository {
    Optional<PartyAuthentication> findByEmailAddress(String emailAddress);
}
