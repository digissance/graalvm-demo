package biz.digissance.graalvmdemo.http;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PersonDTO {

    @NotBlank
    String name;
    @NotBlank
    String lastname;
    @Email
    @NotBlank
    String email;
}
