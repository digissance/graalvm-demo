package biz.digissance.graalvmdemo.jpa.party.address;

import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import biz.digissance.graalvmdemo.jpa.party.JpaParty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class JpaAddressProperty extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private JpaParty party;

    //    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private JpaAddress address;

//    @OneToMany(mappedBy = "addressProperty")
//    private Set<JpaAddressUse> use;

    @ElementCollection
//    @CollectionTable(name = "address_property_use", joinColumns = @JoinColumn(name = "address_property_id"))
//    @Column(name = "phone_number")
    private Set<String> use = new HashSet<>();
}
