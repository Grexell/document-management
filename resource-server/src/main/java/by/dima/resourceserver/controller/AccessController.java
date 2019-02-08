package by.dima.resourceserver.controller;

import by.dima.resourceserver.model.DocumentAccess;
import by.dima.resourceserver.repository.AccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accesses")
public class AccessController {

    private AccessRepository accessRepository;

    @Autowired
    public AccessController(AccessRepository accessRepository) {
        this.accessRepository = accessRepository;
    }

    @GetMapping
    public Iterable<DocumentAccess> getAll() {
        return accessRepository.findAll();
    }
}
