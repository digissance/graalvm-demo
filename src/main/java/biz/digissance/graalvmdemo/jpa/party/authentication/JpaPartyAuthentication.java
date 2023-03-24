package biz.digissance.graalvmdemo.jpa.party.authentication;

import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import biz.digissance.graalvmdemo.jpa.party.JpaParty;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class JpaPartyAuthentication extends BaseEntity {

    @ManyToOne(optional = false)
    private JpaParty party;
}
