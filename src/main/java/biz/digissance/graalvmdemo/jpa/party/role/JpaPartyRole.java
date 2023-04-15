package biz.digissance.graalvmdemo.jpa.party.role;

import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import biz.digissance.graalvmdemo.jpa.party.JpaParty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@IdClass(RolePK.class)
public class JpaPartyRole extends BaseEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String identifier;

    @ManyToOne(optional = false)
    private JpaParty party;

    @ManyToOne(optional = false)
    private JpaPartyRoleType type;

    @PrePersist
    private void createIdentifier() {
        this.identifier = UUID.randomUUID().toString();
    }
}
