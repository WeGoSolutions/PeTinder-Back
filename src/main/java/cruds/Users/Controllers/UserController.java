// Arquivo: src/main/java/cruds/Users/Controllers/UserController.java
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

    /*@Autowired
    private EmailVerificationService emailVerificationService;*/

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = convertDTOToEntity(userDTO);

        ResponseEntity<String> validationResponse = validateAccountCreation(userDTO);
        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(validationResponse.getStatusCode()).body(validationResponse.getBody());
        }

        User savedUser = repository.save(user);
        return ResponseEntity.status(201).body(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = repository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(users);
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
        User updatedUser = repository.save(updateSystem(userOpt, userDTO));
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

    private User updateSystem(Optional<User> userOpt, UserDTO userDTO) {
        User userToUpdate = userOpt.get();
        userToUpdate.setNome(userDTO.getNome());
        userToUpdate.setEmail(userDTO.getEmail());
        userToUpdate.setSenha(userDTO.getSenha());
        userToUpdate.setDataNasc(userDTO.getDataNasc());
        userToUpdate.setCpf(userDTO.getCpf());
        userToUpdate.setCep(userDTO.getCep());
        userToUpdate.setRua(userDTO.getRua());
        userToUpdate.setNumero(userDTO.getNumero());
        userToUpdate.setCidade(userDTO.getCidade());
        userToUpdate.setUf(userDTO.getUf());
        return userToUpdate;
    }

    private ResponseEntity<String> validateAccountCreation(UserDTO userDTO){
        User user = convertDTOToEntity(userDTO);

//        if (user.getDataNasc() == null || user.getNome().replaceAll("\\s+", "").isEmpty() || user.getEmail().replaceAll("\\s+", "").isEmpty() || user.getSenha().replaceAll("\\s+", "").isEmpty() || user.getCpf().replaceAll("\\s+", "").isEmpty()) {
//            return ResponseEntity.status(400).body("Algum campo obrigatório não foi preenchido");
//        }
//
//        List<User> emailValidation = repository.findByEmail(user.getEmail());
//        List<User> cpfValidation = repository.findByCpf(user.getCpf());
//
//        if (!emailValidation.isEmpty()) {
//            return ResponseEntity.status(406).body("Email já cadastrado");
//        } else if (!cpfValidation.isEmpty()) {
//            return ResponseEntity.status(406).body("CPF já cadastrado");
//        } else if (user.getCpf().replaceAll("\\s+", "").length() != 11) {
//            return ResponseEntity.status(411).body("Tamanho CPF inválido");
//        } else if (user.getCpf().toUpperCase().matches(".*[A-Z].*")) {
//            return ResponseEntity.status(401).body("Tem letra no CPF");
//        }
//
//        Integer possicao = user.getEmail().indexOf("@");
//        if (!user.getEmail().contains("@") || !user.getEmail().contains(".") || user.getEmail().indexOf(".", possicao) == -1) {
//            return ResponseEntity.status(400).body("Formatação do email inválido");
//        }
//
//        LocalDate dataNascimento = userDTO.getDataNasc().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        if (LocalDate.now().isBefore(dataNascimento)) {
//            return ResponseEntity.status(401).body("Data de nascimento no futuro");
//        } else if (user.getSenha().replaceAll("\\s+", "").length() <= 6 || !user.getSenha().matches(".*[@#$*!&%].*")) {
//            return ResponseEntity.status(401).body("Senha inválida");
//        }else if (dataNascimento.isAfter(LocalDate.now().minusYears(21))) {
//            return ResponseEntity.status(409).body("Menor de 21 anos");
//        }
//
//        if (user.getNome().matches(".*[0-9].*")){
//            return ResponseEntity.status(401).body("Tem numero no nome");
//        } else if (user.getNome().replaceAll("\\s+", "").length() < 3) {
//            return ResponseEntity.status(409).body("Nome muito curto");
//        }

        return ResponseEntity.status(201).body("Usuário válido");
    }
}