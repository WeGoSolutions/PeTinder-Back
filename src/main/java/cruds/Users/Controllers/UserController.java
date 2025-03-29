// Arquivo: UserController.java
package cruds.Users.Controllers;

import cruds.Users.DTOs.UserRequest;
import cruds.Users.DTOs.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cruds.Users.Tables.User;
import cruds.Users.Repositorys.UserRepository;

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
        user.setId(id); // Ensure the ID is set for the update
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
        User user = new User();
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());
        user.setSenha(request.getSenha());
        user.setDataNasc(request.getDataNasc());
        return user;
    }

    private UserResponse convertEntityToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setNome(user.getNome());
        response.setEmail(user.getEmail());
        return response;
    }
}