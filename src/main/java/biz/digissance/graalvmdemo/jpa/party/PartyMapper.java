package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.domain.party.authentication.EmailPasswordAuthentication;
import biz.digissance.graalvmdemo.http.RegisterRequest;
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
import java.util.Optional;
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
import net.liccioni.archetypes.party.PartyIdentifier;
import net.liccioni.archetypes.party.Person;
import net.liccioni.archetypes.party.PersonName;
import net.liccioni.archetypes.relationship.PartyRole;
import net.liccioni.archetypes.relationship.PartyRoleIdentifier;
import net.liccioni.archetypes.relationship.PartyRoleType;
import net.liccioni.archetypes.uniqueid.UniqueIdentifier;
import org.mapstruct.AfterMapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Condition;
import org.mapstruct.Context;
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

    @SubclassMapping(target = JpaPerson.class, source = Person.class)
    @SubclassMapping(target = JpaOrganization.class, source = Organization.class)
    public abstract JpaParty toPartyJpa(final Party party);


    @Mapping(target = "authentications", qualifiedBy = WithPartyContext.class)
    public abstract void toPartyJpaForUpdate(final Person person,
                                             final @MappingTarget JpaPerson jpaPerson,
                                             final @Context JpaParty context);

    @Mapping(target = "authentications", qualifiedBy = WithPartyContext.class)
    public abstract void toPartyJpaForUpdate(final Organization organization,
                                             final @MappingTarget JpaOrganization jpaOrganization,
                                             final @Context JpaParty context);

    @SubclassMapping(target = JpaEmailAddress.class, source = EmailAddress.class)
    @SubclassMapping(target = JpaGeographicAddress.class, source = GeographicAddress.class)
    public abstract JpaAddress toAddressJpa(final Address address);

    @SubclassMapping(target = JpaEmailPasswordPartyAuthentication.class, source = EmailPasswordAuthentication.class)
    public abstract JpaPartyAuthentication toAuthJpa(final PartyAuthentication authentication);

    @WithPartyContext
    @SubclassMapping(target = JpaEmailPasswordPartyAuthentication.class, source = EmailPasswordAuthentication.class)
    public abstract JpaPartyAuthentication toAuthJpaWithContext(final PartyAuthentication authentication,
                                                                final @Context JpaParty jpaParty);

    @InheritInverseConfiguration(name = "toPartyJpa")
    public abstract Party toPartyDomain(final JpaParty jpaParty);

    @InheritInverseConfiguration(name = "toAuthJpa")
    public abstract PartyAuthentication toAuthDomain(final JpaPartyAuthentication jpaPartyAuthentication);

    @InheritInverseConfiguration(name = "toAddressJpa")
    public abstract Address toAddressDomain(final JpaAddress jpaAddress);

    @Mapping(target = "party", ignore = true)
    public abstract PartyRole toPartyRoleDomain(final JpaPartyRole jpaPartyRole);

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

    public String mapPartyRoleIdentifier(PartyRoleIdentifier value) {
        return Optional.ofNullable(value).map(UniqueIdentifier::getId).orElse(null);
    }

    public PartyRoleIdentifier mapPartyRoleIdentifier(String value) {
        return Optional.ofNullable(value)
                .map(p -> PartyRoleIdentifier.builder().id(p).build()).orElse(null);
    }

    public String mapPartyIdentifier(PartyIdentifier value) {
        return Optional.ofNullable(value).map(UniqueIdentifier::getId).orElse(null);
    }

    public PartyIdentifier mapPartyIdentifier(String value) {
        return Optional.ofNullable(value)
                .map(p -> PartyIdentifier.builder().id(p).build()).orElse(null);
    }

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

    public Person toPersonDomain(RegisterRequest registerRequest, final String password) {
        final EmailAddress emailAddress = EmailAddress.builder()
                .emailAddress(registerRequest.getEmail())
                .build();
        return Person.builder()
                .personName(PersonName.builder()
                        .givenName(registerRequest.getFirstName())
                        .familyName(registerRequest.getLastName())
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
