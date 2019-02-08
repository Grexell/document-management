package by.dima.resourceserver.controller;

import by.dima.resourceserver.model.Document;
import by.dima.resourceserver.model.User;
import by.dima.resourceserver.repository.UserRepository;
import by.dima.resourceserver.service.DocumentService;
import by.dima.resourceserver.service.impl.UserExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/signs")
public class SignController {
    private DocumentService documentService;
    private UserRepository userRepository;

    @Autowired
    public SignController(DocumentService documentService, UserRepository userRepository) {
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public Collection<Document> get(Authentication authentication) {
        return documentService.getSign(userRepository.findById(UserExtractor.extract(authentication).getUsername()).get());
    }

    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void get(Authentication authentication, @PathVariable Long id) {
        documentService.sign(id, userRepository.findById(UserExtractor.extract(authentication).getUsername()).get());
    }
}
