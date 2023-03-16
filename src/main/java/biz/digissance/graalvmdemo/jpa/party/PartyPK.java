package biz.digissance.graalvmdemo.jpa.party;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyPK implements Serializable {

    private Long id;
    private String identifier;
}
