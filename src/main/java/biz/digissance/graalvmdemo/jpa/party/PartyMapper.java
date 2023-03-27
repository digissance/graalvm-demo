package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.domain.party.authentication.EmailPasswordAuthentication;
import biz.digissance.graalvmdemo.http.PersonDTO;
import biz.digissance.graalvmdemo.jpa.DateTimeMapper;
import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import biz.digissance.graalvmdemo.jpa.base.LazyLoadingAwareMapper;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddress;
import biz.digissance.graalvmdemo.jpa.party.address.JpaEmailAddress;
import biz.digissance.graalvmdemo.jpa.party.address.JpaGeographicAddress;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaEmailPasswordPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.organization.JpaOrganization;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPerson;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRole;
import java.util.Collection;
import java.util.Set;
import net.liccioni.archetypes.address.Address;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.address.EmailAddress;
import net.liccioni.archetypes.address.GeographicAddress;
import net.liccioni.archetypes.address.ISOCountryCode;
import net.liccioni.archetypes.address.Locale;
import net.liccioni.archetypes.party.Organization;
import net.liccioni.archetypes.party.Party;
import net.liccioni.archetypes.party.PartyAuthentication;
import net.liccioni.archetypes.party.Person;
import net.liccioni.archetypes.party.PersonName;
import net.liccioni.archetypes.relationship.PartyRole;
import net.liccioni.archetypes.relationship.PartyRoleType;
import org.mapstruct.AfterMapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Condition;
import org.mapstruct.Context;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring",
        uses = {DateTimeMapper.class, JpaEntityFactory.class},
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class PartyMapper implements LazyLoadingAwareMapper {

    @SubclassMapping(target = Person.class, source = JpaPerson.class)
    @SubclassMapping(target = Organization.class, source = JpaOrganization.class)
    public abstract Party toPartyDomain(final JpaParty party);

    @Mapping(target = "personName.validFrom", source = "personName.validFrom")
    @Mapping(target = "personName.validTo", source = "personName.validTo")
    @Mapping(target = "partyIdentifier.id", source = "identifier")
    public abstract Person toPersonDomain(JpaPerson jpaPerson);

    @InheritInverseConfiguration(name = "toPersonDomain")
    public abstract JpaPerson toPersonJpa(Person person, @Context JpaParty context);

    @InheritConfiguration(name = "toPersonJpa")
//    @Mapping(target = "addressProperties[].address", qualifiedByName = "toAddressWithContextJpa")
    public abstract void toPersonJpa(Person person, @MappingTarget JpaPerson target, @Context JpaParty context);

//    protected abstract JpaAddressProperty addressPropertiesToJpaAddressProperty1(AddressProperties addressProperties,
//                                                                                 @Context JpaEntityFactory factory);

    @SubclassMapping(target = EmailAddress.class, source = JpaEmailAddress.class)
    @SubclassMapping(target = GeographicAddress.class, source = JpaGeographicAddress.class)
    public abstract Address toAddressDomain(JpaAddress address);

    public String toJpaCountry(Locale country) {
        return country.getIdentifier();
    }

    public Locale toCountry(String countryCode) {
        final var locale = new java.util.Locale("", countryCode);
        return ISOCountryCode.builder()
                .name(locale.getDisplayCountry())
                .identifier(countryCode)
                .build();
    }

//    @InheritInverseConfiguration(name = "toAddressDomain")
//    public abstract JpaAddress toAddressJpa(Address address);

    @InheritInverseConfiguration(name = "toAddressDomain")
//    @Named("toAddressWithContextJpa")
    public abstract JpaAddress toAddressJpa(Address address, @Context JpaParty context);

    @Mapping(target = "identifier.id", source = "identifier")
    @Mapping(target = "party", ignore = true)
    public abstract PartyRole toPartyRoleDomain(JpaPartyRole role);

    @InheritInverseConfiguration(name = "toPartyRoleDomain")
    @Mapping(target = "party", ignore = true)
    public abstract JpaPartyRole toPartyRoleJpa(PartyRole role, @Context JpaParty context);

    @SubclassMapping(target = EmailPasswordAuthentication.class, source = JpaEmailPasswordPartyAuthentication.class)
    public abstract PartyAuthentication toAuthDomain(JpaPartyAuthentication authentication);

    @InheritInverseConfiguration(name = "toAuthDomain")
    public abstract JpaPartyAuthentication toAuthEntity(PartyAuthentication authentication);

    @Condition
    public boolean isNotLazyLoadedCollection(Collection<? extends BaseEntity> sourceCollection) {
        return isNotLazyLoaded(sourceCollection);
    }

    @AfterMapping
    public void setParty(Party party, @MappingTarget JpaParty target) {
        target.getAddressProperties().forEach(p -> p.setParty(target));
        target.getAuthentications().forEach(p -> p.setParty(target));
        target.getRoles().forEach(p -> p.setParty(target));
    }

    public Person toPersonDomain(PersonDTO personDto, final String password) {
        final EmailAddress emailAddress = EmailAddress.builder()
                .emailAddress(personDto.getEmail())
                .build();
        return Person.builder()
                .personName(PersonName.builder()
                        .givenName(personDto.getFirstName())
                        .familyName(personDto.getLastName())
                        .build())
                .addressProperties(Set.of(AddressProperties.builder()
                        .use(Set.of("authentication"))
                        .address(emailAddress)
                        .build()))
                .authentications(Set.of(EmailPasswordAuthentication.builder()
                        .emailAddress(emailAddress.getEmailAddress())
                        .password(password)
                        .build()))
                .roles(Set.of(PartyRole.builder()
                        .type(PartyRoleType.builder()
                                .name("USER")
                                .description("Basic user")
                                .build())
                        .build()))
                .build();
    }
}
