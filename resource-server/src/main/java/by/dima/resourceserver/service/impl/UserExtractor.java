package by.dima.resourceserver.service.impl;

import by.dima.resourceserver.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public class UserExtractor {
    public static User extract(Authentication authentication) {
        return (User) ((OAuth2AuthenticationDetails) authentication.getDetails()).getDecodedDetails();
    }
}
