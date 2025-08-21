package cruds.Users.controller;

import cruds.Users.controller.dto.request.*;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.controller.dto.response.UserResponseUrlDTO;
import cruds.Users.entity.User;
import cruds.Users.mapper.UserMapper;
import cruds.Users.service.UserAuthenticationService;
import cruds.Users.service.UserCommandService;
import cruds.Users.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuario", description = "Endpoints relacionados ao gerenciamento de usuários.")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService commandService;
    private final UserQueryService queryService;
    private final UserAuthenticationService authService;
    private final UserMapper mapper;
    // private final UserImageService imageService; // Seria injetado aqui

    @Operation(summary = "Cria um novo usuário")
    @PostMapping
    public ResponseEntity<UserResponseCadastroDTO> createUser(@Valid @RequestBody UserRequestCriarDTO userRequest) {
        User savedUser = commandService.createUser(userRequest);
        return ResponseEntity.status(201).body(mapper.toCadastroResponse(savedUser));
    }

    @Operation(summary = "Realiza login do usuário")
    @PostMapping("/login")
    public ResponseEntity<UserResponseLoginDTO> login(@RequestBody @Valid UserRequestLoginDTO loginDTO) {
        User user = queryService.findUserByEmail(loginDTO.getEmail());
        String token = authService.login(loginDTO.getEmail(), loginDTO.getSenha());
        return ResponseEntity.ok(mapper.toLoginResponse(user, token));
    }

    @Operation(summary = "Atualiza informações opcionais do usuário")
    @PutMapping("/{id}/optional")
    public ResponseEntity<UserResponseCadastroDTO> updateOptionalInfo(
            @PathVariable Integer id, @Valid @RequestBody UserRequestOptionalDTO optionalDto) {
        User updatedUser = commandService.updateOptionalInfo(id, optionalDto);
        return ResponseEntity.ok(mapper.toCadastroResponse(updatedUser));
    }

    @Operation(summary = "Lista todos os usuários")
    @GetMapping
    public ResponseEntity<List<UserResponseCadastroDTO>> getAllUsers() {
        List<User> users = queryService.findAllUsers();
        List<UserResponseCadastroDTO> response = users.stream()
                .map(mapper::toCadastroResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Busca usuário por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseCadastroDTO> getUserById(@PathVariable Integer id) {
        User user = queryService.findUserById(id);
        return ResponseEntity.ok(mapper.toCadastroResponse(user));
    }

    @Operation(summary = "Atualiza informações do usuário")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseCadastroDTO> updateUser(
            @PathVariable Integer id, @Valid @RequestBody UserRequestUpdateDTO updateDto) {
        User updatedUser = commandService.updateUser(id, updateDto);
        return ResponseEntity.accepted().body(mapper.toCadastroResponse(updatedUser));
    }

    @Operation(summary = "Exclui o usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        commandService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // endpoints de imagem chamariam o UserImageService

    @Operation(summary = "Valida e-mail do usuário")
    @GetMapping("/{email}/validar-email")
    public ResponseEntity<UserResponseCadastroDTO> validarEmail(@PathVariable String email) {
        User user = queryService.findUserByEmail(email);
        return ResponseEntity.ok(mapper.toCadastroResponse(user));
    }

    @Operation(summary = "Atualiza a senha do usuário")
    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Integer id, @Valid @RequestBody UserRequestUpdatePasswordDTO req) {
        authService.updatePassword(id, req);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Atualiza o campo userNovo para false")
    @PatchMapping("/{id}/user-novo")
    public ResponseEntity<UserResponseCadastroDTO> atualizarUserNovoParaFalse(@PathVariable Integer id) {
        User updatedUser = commandService.setUserNovoToFalse(id);
        return ResponseEntity.ok(mapper.toCadastroResponse(updatedUser));
    }

    @Operation(summary = "(Dev) Deleta todos os usuários")
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllUsers() {
        commandService.deleteAllUsers();
        return ResponseEntity.noContent().build();
    }
}