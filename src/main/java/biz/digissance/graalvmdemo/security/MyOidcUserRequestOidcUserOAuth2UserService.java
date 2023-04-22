package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.domain.party.PartyService;
import biz.digissance.graalvmdemo.http.OidcRegisterRequest;
import java.util.stream.Collectors;
import net.liccioni.archetypes.relationship.PartyRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class MyOidcUserRequestOidcUserOAuth2UserService
        implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final OidcUserService oidcUserService;
    private final PartyService partyService;

    public MyOidcUserRequestOidcUserOAuth2UserService(final OidcUserService oidcUserService,
                                                      final PartyService partyService) {
        this.oidcUserService = oidcUserService;
        this.partyService = partyService;
    }

    @Override
    public OidcUser loadUser(final OidcUserRequest userRequest)
            throws OAuth2AuthenticationException {
        final var oidcUser = oidcUserService.loadUser(userRequest);
        final var email = oidcUser.getEmail();
        final var party = partyService.register(OidcRegisterRequest.builder()
                .email(email)
                .firstName(oidcUser.getGivenName())
                .lastName(oidcUser.getFamilyName())
                .provider(userRequest.getClientRegistration().getClientName())
                .build());
        final var authorities = party.getRoles().stream()
                .map(PartyRole::getName).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        final var idToken = OidcIdToken.withTokenValue(oidcUser.getIdToken().getTokenValue())
                .claim("sub", party.getIdentifier().getId()).build();
        return new DefaultOidcUser(authorities, idToken);
    }
}
