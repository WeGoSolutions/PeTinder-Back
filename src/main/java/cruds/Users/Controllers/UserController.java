// Arquivo: src/main/java/cruds/Users/Controllers/UserController.java
package cruds.Users.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cruds.Users.DTOs.UserDTO;
import cruds.Users.Entities.User;
import cruds.Users.Repositories.UserRepository;

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
        return User.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .dataNasc(dto.getDataNasc())
                .cpf(dto.getCpf())
                .cep(dto.getCep())
                .rua(dto.getRua())
                .numero(dto.getNumero())
                .cidade(dto.getCidade())
                .uf(dto.getUf())
                .build();
    }

    private User updateSystem(Optional<User> userOpt, UserDTO userDTO) {
        return userOpt.get().toBuilder()
                .nome(userDTO.getNome())
                .email(userDTO.getEmail())
                .senha(userDTO.getSenha())
                .dataNasc(userDTO.getDataNasc())
                .cpf(userDTO.getCpf())
                .cep(userDTO.getCep())
                .rua(userDTO.getRua())
                .numero(userDTO.getNumero())
                .cidade(userDTO.getCidade())
                .uf(userDTO.getUf())
                .build();
    }

    private ResponseEntity<String> validateAccountCreation(UserDTO userDTO){
        User user = convertDTOToEntity(userDTO);

        if (user.getDataNasc() == null || user.getNome().replaceAll("\\s+", "").isEmpty() || user.getEmail().replaceAll("\\s+", "").isEmpty() || user.getSenha().replaceAll("\\s+", "").isEmpty() || user.getCpf().replaceAll("\\s+", "").isEmpty()) {
            return ResponseEntity.status(400).body("Algum campo obrigatório não foi preenchido");
        }

        List<User> emailValidation = repository.findByEmail(user.getEmail());
        List<User> cpfValidation = repository.findByCpf(user.getCpf());

        if (!emailValidation.isEmpty()) {
            return ResponseEntity.status(406).body("Email já cadastrado");
        } else if (!cpfValidation.isEmpty()) {
            return ResponseEntity.status(406).body("CPF já cadastrado");
        } else if (user.getCpf().replaceAll("\\s+", "").length() != 11) {
            return ResponseEntity.status(411).body("Tamanho CPF inválido");
        } else if (user.getCpf().toUpperCase().matches(".*[A-Z].*")) {
            return ResponseEntity.status(401).body("Tem letra no CPF");
        }

        Integer possicao = user.getEmail().indexOf("@");
        if (!user.getEmail().contains("@") || !user.getEmail().contains(".") || user.getEmail().indexOf(".", possicao) == -1) {
            return ResponseEntity.status(400).body("Formatação do email inválido");
        }

        LocalDate dataNascimento = userDTO.getDataNasc().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (LocalDate.now().isBefore(dataNascimento)) {
            return ResponseEntity.status(401).body("Data de nascimento no futuro");
        } else if (user.getSenha().replaceAll("\\s+", "").length() <= 6 || !user.getSenha().matches(".*[@#$*!&%].*")) {
            return ResponseEntity.status(401).body("Senha inválida");
        }else if (dataNascimento.isAfter(LocalDate.now().minusYears(21))) {
            return ResponseEntity.status(409).body("Menor de 21 anos");
        }

        if (user.getNome().matches(".*[0-9].*")){
            return ResponseEntity.status(401).body("Tem numero no nome");
        } else if (user.getNome().replaceAll("\\s+", "").length() < 3) {
            return ResponseEntity.status(409).body("Nome muito curto");
        }

        return ResponseEntity.status(200).body("Usuário válido");
    }
}