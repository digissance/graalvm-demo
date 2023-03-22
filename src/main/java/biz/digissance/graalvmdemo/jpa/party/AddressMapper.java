package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.jpa.DateTimeMapper;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddress;
import biz.digissance.graalvmdemo.jpa.party.address.JpaEmailAddress;
import biz.digissance.graalvmdemo.jpa.party.address.JpaGeographicAddress;
import net.liccioni.archetypes.address.Address;
import net.liccioni.archetypes.address.EmailAddress;
import net.liccioni.archetypes.address.GeographicAddress;
import net.liccioni.archetypes.address.ISOCountryCode;
import net.liccioni.archetypes.address.Locale;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring",
        uses = {DateTimeMapper.class,
                JpaEntityFactory.class},
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION
//        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface AddressMapper {

    @SubclassMapping(target = EmailAddress.class, source = JpaEmailAddress.class)
    @SubclassMapping(target = GeographicAddress.class, source = JpaGeographicAddress.class)
    Address toDomain(JpaAddress address);

    EmailAddress toEmailAddressDomain(JpaEmailAddress emailAddress);

    @InheritInverseConfiguration(name = "toDomain")
    JpaAddress toEntity(Address address);

//    JpaEmailAddress toJpaEmailAddress(EmailAddress address);
//    JpaGeographicAddress toJpaGeographicAddress(GeographicAddress address);

    default String toJpaCountry(Locale country) {
        return country.getIdentifier();
    }

    default Locale toCountry(String countryCode) {
        final var locale = new java.util.Locale("", countryCode);
        return ISOCountryCode.builder()
                .name(locale.getDisplayCountry())
                .identifier(countryCode)
                .build();
    }
}
