package biz.digissance.graalvmdemo.jpa.party.authentication;

import java.util.Set;
import net.liccioni.archetypes.relationship.PartyRole;
import org.springframework.beans.factory.annotation.Value;

public interface JpaPartyAuthenticationProjection {

    @Value("#{@partyMapperImpl.toDomain(target.party).roles}")
    Set<PartyRole> getRoles();

    @Value("#{target.password}")
    String getPassword();
}
