package biz.digissance.graalvmdemo.domain.party.authentication;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import net.liccioni.archetypes.party.PartyAuthentication;

@Value
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class OidcAuthentication extends PartyAuthentication {

    @NonNull
    String username;
    @NonNull
    String provider;
}
