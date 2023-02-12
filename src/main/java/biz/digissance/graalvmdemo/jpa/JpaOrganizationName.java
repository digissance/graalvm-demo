package biz.digissance.graalvmdemo.jpa;

import jakarta.persistence.Embeddable;
import java.time.Instant;
import lombok.Data;

@Data
@Embeddable
public class JpaOrganizationName {

    private String organizationName;
    private String use;
    private Instant validFrom;
    private Instant validTo;
}
