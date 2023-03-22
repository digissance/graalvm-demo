package biz.digissance.graalvmdemo.jpa.party.authentication;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class JpaEmailPasswordPartyAuthentication extends JpaPartyAuthentication {

    @Column(unique = true, nullable = false)
    private String emailAddress;

    @Column(nullable = false)
    private String password;
}
