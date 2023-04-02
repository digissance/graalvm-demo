package biz.digissance.graalvmdemo.domain.party;

import biz.digissance.graalvmdemo.http.OidcRegisterRequest;
import biz.digissance.graalvmdemo.http.RegisterRequest;
import net.liccioni.archetypes.party.Party;

public interface PartyService {

    Party register(RegisterRequest registerRequest);

    Party register(OidcRegisterRequest registerRequest);
}
