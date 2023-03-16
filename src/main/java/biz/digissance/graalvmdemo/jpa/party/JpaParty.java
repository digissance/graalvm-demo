package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "party_type")
@IdClass(PartyPK.class)
public abstract class JpaParty extends BaseEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String identifier;

    @OneToMany(mappedBy = "party", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<JpaAddressProperty> addressProperties;

    @PrePersist
    private void createIdentifier() {
        this.identifier = UUID.randomUUID().toString();
    }
}