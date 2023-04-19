package biz.digissance.graalvmdemo.security;

import biz.digissance.graalvmdemo.domain.party.PartyService;
import biz.digissance.graalvmdemo.http.OidcRegisterRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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
        partyService.register(OidcRegisterRequest.builder()
                .email(oidcUser.getEmail())
                .firstName(oidcUser.getGivenName())
                .lastName(oidcUser.getFamilyName())
                .provider(userRequest.getClientRegistration().getClientName())
                .build());
        return oidcUser;
    }
}
