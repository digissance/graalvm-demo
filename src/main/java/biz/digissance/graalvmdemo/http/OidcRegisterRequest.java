package biz.digissance.graalvmdemo.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
//@Value
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class OidcRegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String provider;
}
