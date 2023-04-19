package biz.digissance.graalvmdemo.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;

public class OAuth2AuthorizationResponseTypeDeserializer extends StdDeserializer<OAuth2AuthorizationResponseType> {

    public OAuth2AuthorizationResponseTypeDeserializer() {
        this(null);
    }

    public OAuth2AuthorizationResponseTypeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public OAuth2AuthorizationResponseType deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        return new OAuth2AuthorizationResponseType(node.get("value").asText());
    }
}
