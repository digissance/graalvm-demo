package biz.digissance.graalvmdemo.jpa;

//import javax.persistence.*;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Version;
import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@ToString
@SuperBuilder
@MappedSuperclass
//@NoArgsConstructor
@RequiredArgsConstructor
@EntityListeners({BaseEntityListener.class, AuditingEntityListener.class})
public abstract class BaseEntity {

    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "seq")
    private Long id;

    @Version
    private Long version;

    @CreatedBy
    private String createdBy;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdDate;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private Instant modifiedDate;
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