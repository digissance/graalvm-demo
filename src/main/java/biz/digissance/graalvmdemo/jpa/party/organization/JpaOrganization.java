package biz.digissance.graalvmdemo.jpa.party.organization;

import biz.digissance.graalvmdemo.jpa.party.JpaParty;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString
@DiscriminatorValue("organization")
public class JpaOrganization extends JpaParty {
    @Embedded
    private JpaOrganizationName organizationName;
}
