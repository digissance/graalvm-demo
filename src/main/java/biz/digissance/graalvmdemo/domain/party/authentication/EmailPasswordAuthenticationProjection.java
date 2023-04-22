package biz.digissance.graalvmdemo.domain.party.authentication;

import java.util.Set;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import net.liccioni.archetypes.relationship.PartyRole;

@Value
@Builder
public class EmailPasswordAuthenticationProjection {

    String partyIdentifier;

    @ToString.Exclude
    String password;

    Set<PartyRole> roles;
}
