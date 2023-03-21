package biz.digissance.graalvmdemo.jpa.party.organization;

import jakarta.persistence.Embeddable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class JpaOrganizationName {

    private String organizationName;
    private String use;
    private Instant validFrom;
    private Instant validTo;
}
