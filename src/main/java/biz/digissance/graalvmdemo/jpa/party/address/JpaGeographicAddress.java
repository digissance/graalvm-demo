package biz.digissance.graalvmdemo.jpa.party.address;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private List<String> addressLine = new ArrayList<>();
    private String city;
    private String regionOrState;
    private String zipOrPostCode;
    private String country;
}
