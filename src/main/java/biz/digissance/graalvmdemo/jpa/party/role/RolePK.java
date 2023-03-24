package biz.digissance.graalvmdemo.jpa.party.role;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePK implements Serializable {

    private Long id;
    private String identifier;
}