package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.domain.party.authentication.EmailPasswordAuthentication;
import biz.digissance.graalvmdemo.domain.party.authentication.OidcAuthentication;
import biz.digissance.graalvmdemo.http.OidcRegisterRequest;
import biz.digissance.graalvmdemo.http.RegisterRequest;
import biz.digissance.graalvmdemo.jpa.DateTimeMapper;
import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import biz.digissance.graalvmdemo.jpa.base.LazyLoadingAwareMapper;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddress;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressProperty;
import biz.digissance.graalvmdemo.jpa.party.address.JpaEmailAddress;
import biz.digissance.graalvmdemo.jpa.party.address.JpaGeographicAddress;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaEmailPasswordPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaOidcPartyAuthentication;
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
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
        uses = {DateTimeMapper.class, JpaEntityFactory.class},
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class PartyMapper implements LazyLoadingAwareMapper {

    @Autowired
    private JpaEntityFactory entityFactory;

    @SubclassMapping(target = JpaPerson.class, source = Person.class)
    @SubclassMapping(target = JpaOrganization.class, source = Organization.class)
    public abstract JpaParty toPartyJpa(final Party source);

    @Mapping(target = "authentications", qualifiedBy = WithPartyContext.class)
    public JpaParty toPartyJpaForUpdate(final Party source,
                                        final @MappingTarget JpaParty target,
                                        final @Context JpaParty context) {
        if (source == null) {
            return null;
        }
        if (source instanceof Person) {
            toPersonJpaForUpdate((Person) source, (JpaPerson) target, context);
            return target;
        } else if (source instanceof Organization) {
            toOrganizationJpaForUpdate((Organization) source, (JpaOrganization) target, context);
            return target;
        } else {
            throw new IllegalArgumentException(
                    "Not all subclasses are supported for this mapping. Missing for " + source.getClass());
        }
    }

    @Mapping(target = "authentications", qualifiedBy = WithPartyContext.class)
    @Mapping(target = "addressProperties", qualifiedBy = WithPartyContext.class)
//    @Mapping(target = "roles", qualifiedBy = WithPartyContext.class)
    public abstract void toPersonJpaForUpdate(final Person source,
                                              final @MappingTarget JpaPerson target,
                                              final @Context JpaParty context);

    @Mapping(target = "authentications", qualifiedBy = WithPartyContext.class)
    @Mapping(target = "addressProperties", qualifiedBy = WithPartyContext.class)
//    @Mapping(target = "roles", qualifiedBy = WithPartyContext.class)
    public abstract void toOrganizationJpaForUpdate(final Organization source,
                                                    final @MappingTarget JpaOrganization target,
                                                    final @Context JpaParty context);

//    @WithPartyContext
//    public abstract JpaPartyRole toPartyRoleJpaWithContext(final PartyRole source,
//                                                           final @Context JpaParty context);

    @WithPartyContext
    @Mapping(target = "address", qualifiedBy = WithPartyContext.class)
    public abstract JpaAddressProperty toAddressPropertyJpaWithContext(final AddressProperties source,
                                                                       final @Context JpaParty context);

    @SubclassMapping(target = JpaEmailAddress.class, source = EmailAddress.class)
    @SubclassMapping(target = JpaGeographicAddress.class, source = GeographicAddress.class)
    public abstract JpaAddress toAddressJpa(final Address source);

    @WithPartyContext
    @SubclassMapping(target = JpaEmailAddress.class, source = EmailAddress.class)
    @SubclassMapping(target = JpaGeographicAddress.class, source = GeographicAddress.class)
    public abstract JpaAddress toAddressJpaWithContext(final Address source, final @Context JpaParty context);

    @SubclassMapping(target = JpaEmailPasswordPartyAuthentication.class, source = EmailPasswordAuthentication.class)
    @SubclassMapping(target = JpaOidcPartyAuthentication.class, source = OidcAuthentication.class)
    public abstract JpaPartyAuthentication toAuthJpa(final PartyAuthentication source);

    @AfterMapping
    public void setUserName(final EmailPasswordAuthentication source,
                            final @MappingTarget JpaEmailPasswordPartyAuthentication target) {
        target.setUsername(source.getEmailAddress());
    }

    @AfterMapping
    public void setUserName(final JpaEmailPasswordPartyAuthentication source,
                            final @MappingTarget
                                    EmailPasswordAuthentication.EmailPasswordAuthenticationBuilder<?, ?> target) {
        target.emailAddress(source.getUsername());
    }

    @WithPartyContext
    @SubclassMapping(target = JpaEmailPasswordPartyAuthentication.class, source = EmailPasswordAuthentication.class)
    @SubclassMapping(target = JpaOidcPartyAuthentication.class, source = OidcAuthentication.class)
    public abstract JpaPartyAuthentication toAuthJpaWithContext(final PartyAuthentication source,
                                                                final @Context JpaParty context);

    @InheritInverseConfiguration(name = "toPartyJpa")
    public abstract Party toPartyDomain(final JpaParty source);

    @InheritInverseConfiguration(name = "toAuthJpa")
    public abstract PartyAuthentication toAuthDomain(final JpaPartyAuthentication source);

    @InheritInverseConfiguration(name = "toAddressJpa")
    public abstract Address toAddressDomain(final JpaAddress source);

    @Mapping(target = "party", ignore = true)
    public abstract PartyRole toPartyRoleDomain(final JpaPartyRole source);

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
    public void setParty(final Party source, final @MappingTarget JpaParty target) {
        target.getAddressProperties().forEach(p -> p.setParty(target));
        target.getAuthentications().forEach(p -> p.setParty(target));
        target.getRoles().forEach(p -> p.setParty(target));
    }

    public Person toPersonDomain(RegisterRequest registerRequest) {
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
                .roles(Set.of(PartyRole.builder()
                        .type(PartyRoleType.builder()
                                .name("USER")
                                .description("Basic user")
                                .build())
                        .build()))
                .build();
    }

    public Person toPersonDomain(final OidcRegisterRequest registerRequest) {
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
                .roles(Set.of(PartyRole.builder()
                        .type(PartyRoleType.builder()
                                .name("USER")
                                .description("Basic user")
                                .build())
                        .build()))
                .build();
    }
}
