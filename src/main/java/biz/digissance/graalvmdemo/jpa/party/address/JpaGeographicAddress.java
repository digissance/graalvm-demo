package biz.digissance.graalvmdemo.jpa.party.address;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@DiscriminatorValue("geographic")
public class JpaGeographicAddress extends JpaAddress{

    @ElementCollection
    private Set<String> addressLine = new HashSet<>();
    private String city;
    private String regionOrState;
    private String zipOrPostCode;
}
