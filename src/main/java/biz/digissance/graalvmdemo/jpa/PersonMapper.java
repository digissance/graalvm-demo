package biz.digissance.graalvmdemo.jpa;

import java.time.Instant;
import java.util.Optional;
import net.liccioni.archetypes.common.TimeDate;
import net.liccioni.archetypes.party.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

//@Mapper(uses = {DateMapper.class}, componentModel = "spring")
@Mapper(componentModel = "spring")
//@DecoratedWith(PersonAgeMapper.class)
public interface PersonMapper {

    //    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "personName.validFrom", source = "personName.validFrom", qualifiedByName = "mapTimeDateToInstant")
    @Mapping(target = "personName.validTo", source = "personName.validTo", qualifiedByName = "mapTimeDateToInstant")
    @Mapping(target = "identifier", source = "partyIdentifier.id")
    PersonEntity toPersonEntity(Person person);

    @Mapping(target = "personName.validFrom", source = "personName.validFrom", qualifiedByName = "mapInstantToTimeDate")
    @Mapping(target = "personName.validTo", source = "personName.validTo", qualifiedByName = "mapInstantToTimeDate")
    @Mapping(target = "partyIdentifier.id", source = "identifier")
    Person toPerson(PersonEntity entity);

    @Named("mapTimeDateToInstant")
    default Instant mapTimeDateToInstant(TimeDate timeDate) {
        return Optional.ofNullable(timeDate).map(TimeDate::getValue).orElse(null);
    }

    @Named("mapInstantToTimeDate")
    default TimeDate mapInstantToTimeDate(Instant instant) {
        return Optional.ofNullable(instant).map(p -> TimeDate.builder().value(p).build()).orElse(null);
    }
}
