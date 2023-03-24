package biz.digissance.graalvmdemo.domain;

import java.util.Optional;
import net.liccioni.archetypes.party.Party;
import net.liccioni.archetypes.party.PartyAuthentication;

public interface PartyRepository {
    Optional<PartyAuthentication> findByEmailAddress(String emailAddress);
}
