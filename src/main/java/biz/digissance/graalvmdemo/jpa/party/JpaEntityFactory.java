package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressProperty;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressPropertyRepository;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressRepository;
import biz.digissance.graalvmdemo.jpa.party.address.JpaEmailAddress;
import biz.digissance.graalvmdemo.jpa.party.address.JpaGeographicAddress;
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

//    @ObjectFactory
//    JpaPartyRoleType create(PartyRoleType partyRoleType) {
//        return roleTypeRepository.findByName(partyRoleType.getName()).orElseGet(JpaPartyRoleType::new);
//    }

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
    JpaEmailAddress create(EmailAddress emailAddress, @Context JpaParty target) {
        final var address = Optional.ofNullable(emailAddress).map(Address::getAddress).orElse("");
        return Optional.ofNullable(target)
                .flatMap(p -> p.getAddressProperties().stream()
                        .map(JpaAddressProperty::getAddress)
                        .filter(JpaEmailAddress.class::isInstance)
                        .map(JpaEmailAddress.class::cast)
                        .filter(q -> address.equals(q.getAddress()))
                        .findFirst())
                .orElseGet(JpaEmailAddress::new);
    }

    @ObjectFactory
    JpaGeographicAddress create(GeographicAddress geographicAddress, @Context JpaParty target) {
        final var address = Optional.ofNullable(geographicAddress).map(Address::getAddress).orElse("");
        return Optional.ofNullable(target)
                .flatMap(p -> p.getAddressProperties().stream()
                        .map(JpaAddressProperty::getAddress)
                        .filter(JpaGeographicAddress.class::isInstance)
                        .map(JpaGeographicAddress.class::cast)
                        .filter(q -> address.equals(q.getAddress()))
                        .findFirst())
                .orElseGet(JpaGeographicAddress::new);
    }

    @ObjectFactory
    JpaPartyRoleType create(PartyRoleType partyRoleType, @Context JpaParty context) {
        return roleTypeRepository.findByName(partyRoleType.getName()).orElseGet(JpaPartyRoleType::new);
    }

    @ObjectFactory
    JpaPartyRole create(PartyRole partyRole, @Context JpaParty context) {
        return Optional.ofNullable(partyRole.getIdentifier())
                .map(UniqueIdentifier::getId)
                .flatMap(roleRepository::findByIdentifier)
                .orElseGet(JpaPartyRole::new);
    }
/*
//    @ObjectFactory
    JpaPartyRoleType create(PartyRoleType partyRoleType) {
        return roleTypeRepository.findByName(partyRoleType.getName()).orElseGet(JpaPartyRoleType::new);
    }

//    @ObjectFactory
    JpaPartyRole create(PartyRole partyRole) {
        return Optional.ofNullable(partyRole.getIdentifier())
                .map(UniqueIdentifier::getId)
                .flatMap(roleRepository::findByIdentifier)
                .orElseGet(JpaPartyRole::new);
    }

//    @ObjectFactory
    JpaPerson create(Person person) {
        return Optional.ofNullable(person.getPartyIdentifier())
                .map(UniqueIdentifier::getId)
                .flatMap(personRepository::findByIdentifier)
                .orElseGet(JpaPerson::new);
    }

//

//

//    @ObjectFactory
    JpaAddressProperty create(AddressProperties properties) {
        return addressPropertyRepository.findByAddress_Address(properties.getAddress().getAddress())
                .orElseGet(JpaAddressProperty::new);
    }

//    @ObjectFactory
    JpaEmailPasswordPartyAuthentication create(EmailPasswordAuthentication authentication) {
        return authRepository.findByEmailAddress(authentication.getEmailAddress())
//                .map(JpaEmailPasswordPartyAuthentication.class::cast)
                .orElseGet(JpaEmailPasswordPartyAuthentication::new);
    }*/
}
