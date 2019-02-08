package by.dima.resourceserver.service.impl;

import by.dima.resourceserver.model.DocumentAccess;
import by.dima.resourceserver.model.Post;
import by.dima.resourceserver.model.User;
import by.dima.resourceserver.repository.AccessRepository;
import by.dima.resourceserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {
    private UserRepository userRepository;
    private AccessRepository accessRepository;

    @Autowired
    public CustomAccessTokenConverter(UserRepository userRepository, AccessRepository accessRepository) {
        this.userRepository = userRepository;
        this.accessRepository = accessRepository;
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication authentication = super.extractAuthentication(map);

        String username = authentication.getUserAuthentication().getPrincipal().toString();
        User user;
        if (userRepository.existsById(username)) {
            user = userRepository.findById(username).get();
        } else {
            user = new User(username, map.get("name").toString());
        }

        List<DocumentAccess> accesses = authentication.getAuthorities()
                .stream()
                .map(grantedAuthority -> {
                    String[] status = grantedAuthority.getAuthority().split("_");
                    Optional<DocumentAccess> optional = accessRepository
                            .findByDepartmentAndPost(status[0], status.length > 1? status[1]: "");
                    if (optional.isPresent()) {
                        return optional.get();
                    }
                    DocumentAccess access = new DocumentAccess(status[0], status.length > 1? status[1]: "");
                    return accessRepository.save(access);
                }).collect(Collectors.toList());

        user.setAccesses(accesses);
        userRepository.save(user);

        authentication.setDetails(user);
        return authentication;
    }
}
