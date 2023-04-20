package biz.digissance.graalvmdemo.security;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class MyOAuth2AuthorizationRequest {

    private String clientId;
    private String state;
    private String redirectUri;
    private String authorizationUri;
    private String authorizationRequestUri;

    private List<String> scopes;
    private Map<String, Object> additionalParameters;
    private Map<String, Object> attributes;
}
