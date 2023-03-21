package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.jpa.DateTimeMapper;
import biz.digissance.graalvmdemo.jpa.base.LazyLoadingAwareMapper;
import biz.digissance.graalvmdemo.jpa.party.address.AddressMapper;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressProperty;
import biz.digissance.graalvmdemo.jpa.party.organization.JpaOrganization;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPerson;
import java.util.Collection;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.party.Organization;
import net.liccioni.archetypes.party.Party;
import net.liccioni.archetypes.party.Person;
import org.mapstruct.AfterMapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Condition;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring",
        uses = {DateTimeMapper.class,
                AddressMapper.class},
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface PartyMapper extends LazyLoadingAwareMapper {

    @SubclassMapping(target = Person.class, source = JpaPerson.class)
    @SubclassMapping(target = Organization.class, source = JpaOrganization.class)
    Party toDomain(JpaParty party);

    @Mapping(target = "personName.validFrom", source = "personName.validFrom")
    @Mapping(target = "personName.validTo", source = "personName.validTo")
    @Mapping(target = "partyIdentifier.id", source = "identifier")
    Person toPersonDomain(JpaPerson person);

    @Mapping(target = "organizationName.validFrom", source = "organizationName.validFrom")
    @Mapping(target = "organizationName.validTo", source = "organizationName.validTo")
    @Mapping(target = "partyIdentifier.id", source = "identifier")
    Organization toOrganizationDomain(JpaOrganization organization);

    @InheritInverseConfiguration(name = "toDomain")
    JpaParty toEntity(Party party);

    @InheritInverseConfiguration(name = "toPersonDomain")
//    @Mapping(source = "partyIdentifier.id", target = "identifier")
    JpaPerson toPersonJpa(Person person);

    @InheritInverseConfiguration(name = "toOrganizationDomain")
//    @Mapping(source = "partyIdentifier.id", target = "identifier")
    JpaOrganization toOrganizationJpa(Organization organization);

    @AfterMapping
    default void setParty(Party party, @MappingTarget JpaParty target) {
        target.getAddressProperties().forEach(p -> p.setParty(target));
    }

    @Condition
    default boolean isNotLazyLoadedAddress(Collection<JpaAddressProperty> sourceCollection) {
        return isNotLazyLoaded(sourceCollection);
    }

//    public abstract JpaAddressProperty toAddressPropertyJpa(AddressProperties addressProperties);

//    @InheritConfiguration(name = "toEntity")
//    JpaParty toEntity(Party party, @MappingTarget JpaParty target);

    @InheritConfiguration(name = "toPersonJpa")
    @Mapping(target = "addressProperties", ignore = true)
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    JpaPerson toPersonJpa(Person person, @MappingTarget JpaPerson target);

    @InheritConfiguration(name = "toOrganizationJpa")
//    @Mapping(source = "partyIdentifier.id", target = "identifier")
    JpaOrganization toOrganizationJpa(Organization organization, @MappingTarget JpaOrganization target);

    AddressProperties toAddressProperty(JpaAddressProperty addressProperty);

    void toAddressPropertyJpa(AddressProperties newAddress, @MappingTarget JpaAddressProperty ogAddress);

    JpaAddressProperty toAddressPropertyJpa(AddressProperties newAddress);
}
