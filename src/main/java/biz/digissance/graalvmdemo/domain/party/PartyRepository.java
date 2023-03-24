package biz.digissance.graalvmdemo.domain.party;

import java.util.Optional;
import net.liccioni.archetypes.party.PartyAuthentication;

public interface PartyRepository {
    Optional<PartyAuthentication> findByEmailAddress(String emailAddress);
}
