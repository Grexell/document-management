package by.dima.authserver.controller;

import by.dima.authserver.model.User;
import by.dima.authserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/admin")
public class UserController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{id:.+}")
    public User get(@PathVariable String id) {
        return userRepository.findById(id).get();
    }

    @GetMapping("/roles")
    public Set<String> getRoles() {
        Set<String> result = new HashSet<>();
        StreamSupport.stream(userRepository.findAll().spliterator(), false).forEach(user -> {
            result.addAll(user.getRoles());
        });
        return result;
    }

    @PostMapping("")
    public User create(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PutMapping("/{id:.+}")
    public ResponseEntity update(@PathVariable String id, @RequestBody User user) {
        if (!user.getUsername().equals(id)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity(userRepository.save(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id:.+}")
    public ResponseEntity delete(@PathVariable String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
