package biz.digissance.graalvmdemo.jpa.party.address;

import biz.digissance.graalvmdemo.jpa.DateTimeMapper;
import net.liccioni.archetypes.address.Address;
import net.liccioni.archetypes.address.AddressProperties;
import net.liccioni.archetypes.address.EmailAddress;
import net.liccioni.archetypes.address.GeographicAddress;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring",
        uses = DateTimeMapper.class,
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION)
public interface AddressMapper {

    @SubclassMapping(target = EmailAddress.class, source = JpaEmailAddress.class)
    @SubclassMapping(target = GeographicAddress.class, source = JpaGeographicAddress.class)
    Address toDomain(JpaAddress address);

    EmailAddress toEmailAddressDomain(JpaEmailAddress emailAddress);

    @InheritInverseConfiguration(name = "toDomain")
    JpaAddress toEntity(Address address);

//    AddressProperties toAddressPropertiesDomain(JpaAddressProperty addressProperty);

//    JpaAddressProperty toAddressPropertiesJpa(AddressProperties addressProperty);

//    default String toAddressPropertiesUseDomain(JpaAddressUse use) {
//        return use.getUse();
//    }

//    default JpaAddressUse toAddressPropertiesUseDomain(String use) {
//        final var jpaAddressUse = new JpaAddressUse();
//        jpaAddressUse.setUse(use);
//        return jpaAddressUse;
//    }
}
