// Arquivo: UserController.java
package cruds.Users.controller;

import cruds.Users.controller.dto.UserRequest;
import cruds.Users.controller.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        User user = convertRequestToEntity(userRequest);
        User savedUser = repository.save(user);
        UserResponse userResponse = convertEntityToResponse(savedUser);
        return ResponseEntity.status(201).body(userResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<UserResponse> userResponses = users.stream()
                .map(this::convertEntityToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.status(200).body(userResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        Optional<User> userOpt = repository.findById(id);
        return userOpt.map(user -> ResponseEntity.ok(convertEntityToResponse(user)))
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer id, @Valid @RequestBody UserRequest userRequest) {
        Optional<User> userOpt = repository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        User user = convertRequestToEntity(userRequest);
        user.setId(id);
        User updatedUser = repository.save(user);
        UserResponse userResponse = convertEntityToResponse(updatedUser);
        return ResponseEntity.status(202).body(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        Optional<User> userOpt = repository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        repository.deleteById(id);
        return ResponseEntity.status(204).build();
    }

    private User convertRequestToEntity(UserRequest request) {
        return User.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha())
                .dataNasc(request.getDataNasc())
                .build();
    }

    private UserResponse convertEntityToResponse(User user) {
        return UserResponse.builder()
                .nome(user.getNome())
                .email(user.getEmail())
                .build();
    }
}