package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.domain.EmailPasswordAuthentication;
import biz.digissance.graalvmdemo.jpa.DateTimeMapper;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaEmailPasswordPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.organization.JpaOrganization;
import biz.digissance.graalvmdemo.jpa.party.person.JpaPerson;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRole;
import net.liccioni.archetypes.party.Organization;
import net.liccioni.archetypes.party.Party;
import net.liccioni.archetypes.party.PartyAuthentication;
import net.liccioni.archetypes.party.Person;
import net.liccioni.archetypes.relationship.PartyRole;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring",
        uses = {DateTimeMapper.class, AddressMapper.class, JpaEntityFactory.class},
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface AuthenticationMapper {

    @SubclassMapping(target = EmailPasswordAuthentication.class, source = JpaEmailPasswordPartyAuthentication.class)
    PartyAuthentication toDomain(JpaPartyAuthentication authentication);

    @InheritInverseConfiguration(name = "toDomain")
    JpaPartyAuthentication toEntity(PartyAuthentication authentication);

    /*@SubclassMapping(target = Person.class, source = JpaPerson.class)
    @SubclassMapping(target = Organization.class, source = JpaOrganization.class)
    Party toPartyDomain(JpaParty jpaParty);

    @InheritInverseConfiguration(name = "toPartyDomain")
    JpaParty toPartyEntity(Party party);

    @Mapping(target = "identifier.id", source = "identifier")
    @Mapping(target = "party", ignore = true)
    PartyRole toPartyRoleDomain(JpaPartyRole role);

    @InheritInverseConfiguration(name = "toPartyRoleDomain")
    @Mapping(target = "party", ignore = true)
    JpaPartyRole toPartyRoleEntity(PartyRole role);*/
}
