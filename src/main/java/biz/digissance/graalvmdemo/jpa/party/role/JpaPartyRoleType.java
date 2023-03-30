package biz.digissance.graalvmdemo.jpa.party.role;

import biz.digissance.graalvmdemo.jpa.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class JpaPartyRoleType extends BaseEntity {

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
}
