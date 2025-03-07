// Arquivo: UserController.java
package sptech.school.crud_imagem.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.crud_imagem.DTOs.UserDTO;
import sptech.school.crud_imagem.Tables.User;
import sptech.school.crud_imagem.Repositorys.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        if (userDTO.getNome() == null || userDTO.getEmail() == null || userDTO.getSenha() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User user = convertDTOToEntity(userDTO);
        User savedUser = repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        Optional<User> userOpt = repository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User userToUpdate = userOpt.get();
        userToUpdate.setNome(userDTO.getNome());
        userToUpdate.setEmail(userDTO.getEmail());
        userToUpdate.setSenha(userDTO.getSenha());
        userToUpdate.setDataAniversario(userDTO.getDataAniversario());
        userToUpdate.setCpf(userDTO.getCpf());
        userToUpdate.setCep(userDTO.getCep());
        userToUpdate.setRua(userDTO.getRua());
        userToUpdate.setNumero(userDTO.getNumero());
        userToUpdate.setCidade(userDTO.getCidade());
        userToUpdate.setUf(userDTO.getUf());
        User updatedUser = repository.save(userToUpdate);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        Optional<User> userOpt = repository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private User convertDTOToEntity(UserDTO dto) {
        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha());
        user.setDataAniversario(dto.getDataAniversario());
        user.setCpf(dto.getCpf());
        user.setCep(dto.getCep());
        user.setRua(dto.getRua());
        user.setNumero(dto.getNumero());
        user.setCidade(dto.getCidade());
        user.setUf(dto.getUf());
        return user;
    }
}