// Arquivo: UserController.java
package cruds.Users.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cruds.Users.DTOs.UserDTO;
import cruds.Users.Tables.User;
import cruds.Users.Repositorys.UserRepository;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = convertDTOToEntity(userDTO);
        User savedUser = repository.save(user);
        return ResponseEntity.status(201).body(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(201).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> userOpt = repository.findById(id);
        return userOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @Valid @RequestBody UserDTO userDTO) {
        Optional<User> userOpt = repository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        User user = convertDTOToEntity(userDTO);
        User updatedUser = repository.save(user);
        return ResponseEntity.status(202).body(updatedUser);
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

    private User convertDTOToEntity(UserDTO dto) {
        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha());
        user.setDataNasc(dto.getDataNasc());
        user.setCpf(dto.getCpf());
        user.setCep(dto.getCep());
        user.setRua(dto.getRua());
        user.setNumero(dto.getNumero());
        user.setCidade(dto.getCidade());
        user.setUf(dto.getUf());
        return user;
    }

}