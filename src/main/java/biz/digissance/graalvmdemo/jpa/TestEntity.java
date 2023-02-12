package biz.digissance.graalvmdemo.jpa;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

//@Entity
//@Getter
//@Setter
//@ToString
//@SuperBuilder
//@NoArgsConstructor
public class TestEntity extends BaseEntity {

    @NotBlank
    String param1;
    String param2;
}
