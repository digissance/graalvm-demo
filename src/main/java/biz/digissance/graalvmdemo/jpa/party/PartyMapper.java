package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.domain.EmailPasswordAuthentication;
import biz.digissance.graalvmdemo.http.PersonDTO;
import biz.digissance.graalvmdemo.jpa.DateTimeMapper;
import biz.digissance.graalvmdemo.jpa.base.LazyLoadingAwareMapper;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressProperty;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.organization.JpaOrganization;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPerson;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRole;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.address.EmailAddress;
import net.liccioni.archetypes.party.Organization;
import net.liccioni.archetypes.party.Party;
import net.liccioni.archetypes.party.Person;
import net.liccioni.archetypes.party.PersonName;
import net.liccioni.archetypes.relationship.PartyRole;
import net.liccioni.archetypes.relationship.PartyRoleType;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
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
        uses = {DateTimeMapper.class,
                AddressMapper.class,
                AuthenticationMapper.class,
                JpaEntityFactory.class},
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PartyMapper extends LazyLoadingAwareMapper {

    @SubclassMapping(target = Person.class, source = JpaPerson.class)
    @SubclassMapping(target = Organization.class, source = JpaOrganization.class)
    Party toDomain(JpaParty jpaParty);

    @Mapping(target = "personName.validFrom", source = "personName.validFrom")
    @Mapping(target = "personName.validTo", source = "personName.validTo")
    @Mapping(target = "partyIdentifier.id", source = "identifier")
    Person toPersonDomain(JpaPerson jpaPerson);

    @Mapping(target = "organizationName.validFrom", source = "organizationName.validFrom")
    @Mapping(target = "organizationName.validTo", source = "organizationName.validTo")
    @Mapping(target = "partyIdentifier.id", source = "identifier")
    Organization toOrganizationDomain(JpaOrganization jpaOrganization);

    @InheritInverseConfiguration(name = "toDomain")
    JpaParty toEntity(Party party);

    @InheritInverseConfiguration(name = "toPersonDomain")
//    @Mapping(target = "addressProperties", ignore = true)
//    @Mapping(target = "authentications", ignore = true)
//    @Mapping(target = "roles", ignore = true)
    JpaPerson toPersonJpa(Person person);

    @InheritInverseConfiguration(name = "toOrganizationDomain")
    JpaOrganization toOrganizationJpa(Organization organization);

    @AfterMapping
    default void setParty(Party party, @MappingTarget JpaParty target) {
        Optional.ofNullable(target.getAddressProperties())
                .ifPresent(address -> address.forEach(p -> p.setParty(target)));
        Optional.ofNullable(target.getAuthentications())
                .ifPresent(auths -> auths.forEach(p -> p.setParty(target)));
        Optional.ofNullable(target.getRoles())
                .ifPresent(roles -> roles.forEach(p -> p.setParty(target)));
    }

    @Condition
    default boolean isNotLazyLoadedAddress(Collection<JpaAddressProperty> sourceCollection) {
        return isNotLazyLoaded(sourceCollection);
    }

    @Condition
    default boolean isNotLazyLoadedRoles(Collection<JpaPartyRole> sourceCollection) {
        return isNotLazyLoaded(sourceCollection);
    }

    @Condition
    default boolean isNotLazyLoadedAuthentications(Collection<JpaPartyAuthentication> sourceCollection) {
        return isNotLazyLoaded(sourceCollection);
    }

    @InheritConfiguration(name = "toPersonJpa")
//    @Mapping(target = "addressProperties", ignore = true)
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toPersonJpa(Person person, @MappingTarget JpaPerson target);

    @InheritConfiguration(name = "toOrganizationJpa")
    JpaOrganization toOrganizationJpa(Organization organization, @MappingTarget JpaOrganization target);

    AddressProperties toAddressProperty(JpaAddressProperty jpaAddressProperty);

    JpaAddressProperty toAddressPropertyJpa(AddressProperties newAddress, @Context JpaParty jpaParty);

    @BeforeMapping
    default void setParty(@Context JpaParty jpaParty, @MappingTarget JpaAddressProperty target) {
        target.setParty(jpaParty);
        jpaParty.getAddressProperties().add(target);
    }

    @Mapping(target = "identifier.id", source = "identifier")
    @Mapping(target = "party", ignore = true)
    PartyRole toPartyRoleDomain(JpaPartyRole role);

    @InheritInverseConfiguration(name = "toPartyRoleDomain")
    @Mapping(target = "party", ignore = true)
    JpaPartyRole toPartyRoleEntity(PartyRole role);

    @BeforeMapping
    default void setParty(JpaPartyRole jpaPartyRole, @MappingTarget PartyRole.PartyRoleBuilder target) {
//        target.party(party);
        System.out.println(jpaPartyRole);
    }

    default Person toPersonDomain(PersonDTO personDto) {
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
                        .password(personDto.getPassword())
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
