package biz.digissance.graalvmdemo.jpa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@DiscriminatorValue("Person")
public class PersonEntity extends PartyEntity {
    @Embedded
    private JpaPersonName personName;
}
