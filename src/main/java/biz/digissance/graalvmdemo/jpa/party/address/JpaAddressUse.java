package biz.digissance.graalvmdemo.jpa.party.address;

import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@Entity
//@Getter
//@Setter
//@ToString
public class JpaAddressUse extends BaseEntity {

//    @ManyToOne
    private JpaAddressProperty addressProperty;

    private String use;
}
