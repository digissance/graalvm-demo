package biz.digissance.graalvmdemo.domain.party.authentication;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import net.liccioni.archetypes.party.Party;
import net.liccioni.archetypes.party.PartyAuthentication;

@Value
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class EmailPasswordAuthentication extends PartyAuthentication {

    String emailAddress;
    String password;
//    Party party;
}
