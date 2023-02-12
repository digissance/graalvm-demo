package biz.digissance.graalvmdemo.jpa;

//import javax.persistence.*;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Party_Type")
@IdClass(PartyPK.class)
public abstract class PartyEntity extends BaseEntity {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "identifier_seq")
    @GenericGenerator(
            name = "identifier_seq",
            strategy = "biz.digissance.graalvmdemo.jpa.UUIDSequenceIdGenerator")
    private String identifier;

//    @PrePersist
//    private void createIdentifier(){
//        this.identifier = UUID.randomUUID().toString();
//    }
}

/*

abstract class BaseEntity(
    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "seq")
    var id: Long? = null,

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    var createdDate: Instant? = null,

    @LastModifiedDate
    @Column(name = "modified_date")
    var modifiedDate: Instant? = null,

    @Version
    var version: Long = 0
)
 */