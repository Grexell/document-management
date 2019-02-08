package by.dima.resourceserver.controller;

import by.dima.resourceserver.model.Document;
import by.dima.resourceserver.model.User;
import by.dima.resourceserver.repository.UserRepository;
import by.dima.resourceserver.service.DocumentService;
import by.dima.resourceserver.service.impl.UserExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/archive")
public class ArchiveController {
    private DocumentService documentService;
    private UserRepository userRepository;

    @Autowired
    public ArchiveController(DocumentService documentService, UserRepository userRepository) {
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public List<Document> getArchived(Authentication authentication){
        return documentService.getArchived(userRepository.findById(UserExtractor.extract(authentication).getUsername()).get());
    }

    @PostMapping("/{id}")
    public void archive(@PathVariable Long id, Authentication authentication) {
        documentService.archive(id, UserExtractor.extract(authentication));
    }
}
