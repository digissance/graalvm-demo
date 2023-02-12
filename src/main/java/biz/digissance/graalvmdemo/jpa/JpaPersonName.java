package biz.digissance.graalvmdemo.jpa;

import jakarta.persistence.Embeddable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class JpaPersonName {

    private String familyName;
    private String givenName;
    private Instant validFrom;
    private Instant validTo;
}
