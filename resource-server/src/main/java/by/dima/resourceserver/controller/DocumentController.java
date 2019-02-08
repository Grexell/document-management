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

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private DocumentService documentService;
    private UserRepository userRepository;

    @Autowired
    public DocumentController(DocumentService documentService, UserRepository userRepository) {
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public List<Document> get(Authentication authentication) {
        return documentService.getAll(UserExtractor.extract(authentication).getUsername());
    }

    @GetMapping("/active")
    public List<Document> getActive(Authentication authentication) {
        return documentService.getActive(userRepository.findById(UserExtractor.extract(authentication).getUsername()).get());
    }

    @GetMapping("/{id}")
    public Document get(@PathVariable Long id, Authentication authentication) {
        return documentService.get(id, UserExtractor.extract(authentication));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Document document, Authentication authentication) {
        documentService.save(document, UserExtractor.extract(authentication));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id, Authentication authentication) {
        documentService.delete(id, UserExtractor.extract(authentication));
    }
}
