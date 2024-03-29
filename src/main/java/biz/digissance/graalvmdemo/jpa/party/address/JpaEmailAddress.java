package biz.digissance.graalvmdemo.jpa.party.address;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@DiscriminatorValue("email")
public class JpaEmailAddress extends JpaAddress {

    @Column(unique = true)
    private String emailAddress;
}
