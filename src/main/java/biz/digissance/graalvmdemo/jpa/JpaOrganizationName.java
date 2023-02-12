package biz.digissance.graalvmdemo.jpa;

import jakarta.persistence.Embeddable;
import java.time.Instant;
import lombok.Data;

@Data
@Embeddable
public class JpaPersonName {

    private String familyName;
    private String givenName;
    private Instant validFrom;
    private Instant validTo;
}
