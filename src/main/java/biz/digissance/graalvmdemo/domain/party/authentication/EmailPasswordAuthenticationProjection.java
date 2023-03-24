package biz.digissance.graalvmdemo.domain.party.authentication;

import java.util.Set;
import lombok.Builder;
import lombok.Value;
import net.liccioni.archetypes.relationship.PartyRole;

@Value
@Builder
public class EmailPasswordAuthenticationProjection {

    String password;

    Set<PartyRole> roles;
}
