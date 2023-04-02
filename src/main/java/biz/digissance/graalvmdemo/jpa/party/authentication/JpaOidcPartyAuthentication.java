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
public class JpaOidcPartyAuthentication extends JpaPartyAuthentication {

    @Column(nullable = false)
    @ToString.Exclude
    private String provider;
}
