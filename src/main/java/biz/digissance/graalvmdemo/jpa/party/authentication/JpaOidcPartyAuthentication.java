package biz.digissance.graalvmdemo.jpa.party.authentication;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@DiscriminatorValue("oidc")
public class JpaOidcPartyAuthentication extends JpaPartyAuthentication {

    @Column
    @ToString.Exclude
    private String provider;
}
