package biz.digissance.graalvmdemo.jpa;

import java.time.Instant;
import java.util.Optional;
import net.liccioni.archetypes.common.TimeDate;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DateTimeMapper {

    @Named("map")
    default Instant map(TimeDate timeDate) {
        return Optional.ofNullable(timeDate).map(TimeDate::getValue).orElse(null);
    }

    @Named("map")
    default TimeDate map(Instant instant) {
        return Optional.ofNullable(instant).map(p -> TimeDate.builder().value(p).build()).orElse(null);
    }
}
