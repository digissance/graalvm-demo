package biz.digissance.graalvmdemo.domain.party.authentication;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import net.liccioni.archetypes.party.PartyAuthentication;

@Value
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class EmailPasswordAuthentication extends PartyAuthentication {

    @NonNull
    String emailAddress;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    String password;
}
