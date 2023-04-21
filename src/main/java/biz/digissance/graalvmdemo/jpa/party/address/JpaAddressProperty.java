package biz.digissance.graalvmdemo.jpa.party.address;

import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import biz.digissance.graalvmdemo.jpa.party.JpaParty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
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

    @ManyToOne(optional = false)
    private JpaParty party;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private JpaAddress address;

    @ElementCollection
    private Set<String> use = new HashSet<>();
}
