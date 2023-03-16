package biz.digissance.graalvmdemo.jpa.party.person;

import biz.digissance.graalvmdemo.jpa.party.JpaParty;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@DiscriminatorValue("person")
public class JpaPerson extends JpaParty {
    @Embedded
    private JpaPersonName personName;
}
