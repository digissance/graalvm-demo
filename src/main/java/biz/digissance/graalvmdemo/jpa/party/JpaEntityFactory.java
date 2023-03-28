package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.domain.party.authentication.EmailPasswordAuthentication;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressProperty;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressPropertyRepository;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressRepository;
import biz.digissance.graalvmdemo.jpa.party.address.JpaEmailAddress;
import biz.digissance.graalvmdemo.jpa.party.address.JpaGeographicAddress;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaEmailPasswordPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthenticationRepository;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPersonRepository;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRole;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleRepository;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleType;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRoleTypeRepository;
import java.util.Optional;
import net.liccioni.archetypes.address.Address;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.address.EmailAddress;
import net.liccioni.archetypes.address.GeographicAddress;
import net.liccioni.archetypes.relationship.PartyRole;
import net.liccioni.archetypes.relationship.PartyRoleType;
import net.liccioni.archetypes.uniqueid.UniqueIdentifier;
import org.mapstruct.Context;
import org.mapstruct.ObjectFactory;
import org.springframework.stereotype.Service;

@Service
public class JpaEntityFactory {

    private final JpaPartyRoleTypeRepository roleTypeRepository;
    private final JpaPersonRepository personRepository;
    private final JpaAddressRepository addressRepository;
    private final JpaAddressPropertyRepository addressPropertyRepository;
    private final JpaPartyRoleRepository roleRepository;
    private final JpaPartyAuthenticationRepository authRepository;
    private final JpaPartyRepository partyRepository;

    public JpaEntityFactory(final JpaPartyRoleTypeRepository roleTypeRepository,
                            final JpaPersonRepository personRepository,
                            final JpaAddressRepository addressRepository,
                            final JpaAddressPropertyRepository addressPropertyRepository,
                            final JpaPartyRoleRepository roleRepository,
                            final JpaPartyAuthenticationRepository authRepository,
                            final JpaPartyRepository partyRepository) {
        this.roleTypeRepository = roleTypeRepository;
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
        this.addressPropertyRepository = addressPropertyRepository;
        this.roleRepository = roleRepository;
        this.authRepository = authRepository;
        this.partyRepository = partyRepository;
    }

    @ObjectFactory
    JpaAddressProperty create(AddressProperties addressProperties, @Context JpaParty target) {
        final var address = Optional.ofNullable(addressProperties)
                .map(AddressProperties::getAddress).map(Address::getAddress).orElse("");
        return Optional.ofNullable(target)
                .flatMap(p -> p.getAddressProperties().stream()
                        .filter(q -> address.equals(q.getAddress().getAddress()))
                        .findFirst())
                .orElseGet(JpaAddressProperty::new);
    }

    @ObjectFactory
    JpaEmailPasswordPartyAuthentication create(EmailPasswordAuthentication authentication, @Context JpaParty target) {
        return authRepository.findByEmailAddress(authentication.getEmailAddress())
                .orElseGet(JpaEmailPasswordPartyAuthentication::new);
    }

    @ObjectFactory
    JpaEmailAddress create(EmailAddress emailAddress) {
        return (JpaEmailAddress) addressRepository.findByAddress(emailAddress.getAddress())
                .orElseGet(JpaEmailAddress::new);
    }

    @ObjectFactory
    JpaGeographicAddress create(GeographicAddress geographicAddress) {
        return (JpaGeographicAddress) addressRepository.findByAddress(geographicAddress.getAddress())
                .orElseGet(JpaGeographicAddress::new);
    }

    @ObjectFactory
    JpaPartyRoleType create(PartyRoleType partyRoleType) {
        return roleTypeRepository.findByName(partyRoleType.getName()).orElseGet(JpaPartyRoleType::new);
    }

    @ObjectFactory
    JpaPartyRole create(PartyRole partyRole, @Context JpaParty context) {
        return Optional.ofNullable(partyRole.getIdentifier())
                .map(UniqueIdentifier::getId)
                .flatMap(roleRepository::findByIdentifier)
                .orElseGet(JpaPartyRole::new);
    }
}
