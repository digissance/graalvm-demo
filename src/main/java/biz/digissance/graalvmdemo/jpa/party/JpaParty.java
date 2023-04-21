package biz.digissance.graalvmdemo.jpa.party;

import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import biz.digissance.graalvmdemo.jpa.party.address.JpaAddressProperty;
import biz.digissance.graalvmdemo.jpa.party.authentication.JpaPartyAuthentication;
import biz.digissance.graalvmdemo.jpa.party.role.JpaPartyRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import java.util.HashSet;
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
@NamedEntityGraph(name = "Party.attributes",
        attributeNodes = {
                @NamedAttributeNode(value = "authentications"),
                @NamedAttributeNode(value = "addressProperties", subgraph = "addressProperties-subgraph"),
                @NamedAttributeNode(value = "roles", subgraph = "roles-subgraph"),
        },
        subgraphs = {
                @NamedSubgraph(name = "addressProperties-subgraph",
                        attributeNodes = {@NamedAttributeNode("address"), @NamedAttributeNode("use")}),
                @NamedSubgraph(name = "roles-subgraph", attributeNodes = {@NamedAttributeNode("type")}),
        }
)
public abstract class JpaParty extends BaseEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String identifier;

    private String name;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JpaAddressProperty> addressProperties = new HashSet<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JpaPartyAuthentication> authentications = new HashSet<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JpaPartyRole> roles = new HashSet<>();

    @PrePersist
    private void createIdentifier() {
        this.identifier = UUID.randomUUID().toString();
    }
}