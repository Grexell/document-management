package by.dima.authserver.token;

import by.dima.authserver.model.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();

        additionalInfo.put("name", ((User)oAuth2Authentication.getUserAuthentication().getPrincipal()).getName());
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(
            additionalInfo);
        return oAuth2AccessToken;
    }
}
