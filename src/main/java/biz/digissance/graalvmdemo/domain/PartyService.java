package biz.digissance.graalvmdemo.domain;

import biz.digissance.graalvmdemo.http.PersonDTO;
import net.liccioni.archetypes.party.Party;

public interface PartyService {

    Party register(PersonDTO personDTO);
}
