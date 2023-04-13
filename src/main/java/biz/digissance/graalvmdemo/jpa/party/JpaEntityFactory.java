package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.domain.party.authentication.EmailPasswordAuthentication;
import biz.digissance.graalvmdemo.domain.party.authentication.OidcAuthentication;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressProperty;
import biz.digissance.graalvmdemo.jpa.party.address.JpaEmailAddress;
import biz.digissance.graalvmdemo.jpa.party.address.JpaGeographicAddress;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaEmailPasswordPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaOidcPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRole;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleType;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleTypeRepository;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.address.EmailAddress;
import net.liccioni.archetypes.address.GeographicAddress;
import net.liccioni.archetypes.relationship.PartyRole;
import net.liccioni.archetypes.relationship.PartyRoleType;
import org.mapstruct.Context;
import org.mapstruct.ObjectFactory;
import org.springframework.stereotype.Service;

@Service
public class JpaEntityFactory {

    private final JpaPartyRoleTypeRepository partyRoleTypeRepository;

    public JpaEntityFactory(
            final JpaPartyRoleTypeRepository partyRoleTypeRepository) {
        this.partyRoleTypeRepository = partyRoleTypeRepository;
    }

    @ObjectFactory
    public JpaAddressProperty create(AddressProperties addressProperties, @Context JpaParty context) {
        return context.getAddressProperties().stream()
                .filter(address -> address.getAddress().getAddress()
                        .equals(addressProperties.getAddress().getAddress()))
                .findFirst().orElseGet(JpaAddressProperty::new);
    }

    @ObjectFactory
    public JpaEmailPasswordPartyAuthentication create(EmailPasswordAuthentication authentication,
                                                      @Context JpaParty context) {
        return context.getAuthentications().stream()
                .filter(JpaEmailPasswordPartyAuthentication.class::isInstance)
                .map(JpaEmailPasswordPartyAuthentication.class::cast)
                .filter(auth -> auth.getUsername().equals(authentication.getEmailAddress()))
                .findFirst().orElseGet(JpaEmailPasswordPartyAuthentication::new);
    }

    @ObjectFactory
    public JpaOidcPartyAuthentication create(OidcAuthentication authentication, @Context JpaParty context) {
        return context.getAuthentications().stream()
                .filter(JpaOidcPartyAuthentication.class::isInstance)
                .map(JpaOidcPartyAuthentication.class::cast)
                .filter(auth -> auth.getUsername().equals(authentication.getUsername()))
                .findFirst().orElseGet(JpaOidcPartyAuthentication::new);
    }

    @ObjectFactory
    public JpaEmailAddress create(EmailAddress emailAddress, @Context JpaParty context) {
        return context.getAddressProperties().stream()
                .map(JpaAddressProperty::getAddress)
                .filter(JpaEmailAddress.class::isInstance)
                .map(JpaEmailAddress.class::cast)
                .filter(address -> address.getAddress().equals(emailAddress.getAddress()))
                .findFirst().orElseGet(JpaEmailAddress::new);
    }

    @ObjectFactory
    public JpaGeographicAddress create(GeographicAddress geographicAddress, @Context JpaParty context) {
        return context.getAddressProperties().stream()
                .map(JpaAddressProperty::getAddress)
                .filter(JpaGeographicAddress.class::isInstance)
                .map(JpaGeographicAddress.class::cast)
                .filter(address -> address.getAddress().equals(geographicAddress.getAddress()))
                .findFirst().orElseGet(JpaGeographicAddress::new);
    }

    @ObjectFactory
    public JpaPartyRoleType create(PartyRoleType partyRoleType, @Context JpaParty context) {
        return context.getRoles().stream()
                .map(JpaPartyRole::getType)
                .filter(type -> type.getName().equals(partyRoleType.getName()))
                .findFirst()
                .orElseGet(() -> partyRoleTypeRepository.findByName(partyRoleType.getName())
                        .orElseGet(JpaPartyRoleType::new));
    }

    @ObjectFactory
    public JpaPartyRole create(PartyRole partyRole, @Context JpaParty context) {
        return context.getRoles().stream().filter(p -> p.getIdentifier().equals(partyRole.getIdentifier().getId()))
                .findFirst().orElseGet(JpaPartyRole::new);
    }
}
