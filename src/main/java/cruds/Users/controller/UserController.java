package cruds.Users.controller;

import cruds.Users.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import cruds.Users.controller.dto.request.*;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuario", description = "Endpoints relacionados ao gerenciamento de usuários.")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Cria um novo usuário")
    @PostMapping
    public ResponseEntity<UserResponseCadastroDTO> createUser(@Valid @RequestBody UserRequestCriarDTO userRequest) {
        var savedUser = userService.createUser(userRequest);
        return ResponseEntity.status(201).body(savedUser);
    }

    @Operation(summary = "Realiza login do usuário")
    @PostMapping("/login")
    public ResponseEntity<UserResponseLoginDTO> login(@Valid @RequestBody UserRequestLoginDTO loginRequest) {
        var user = userService.login(loginRequest.getEmail(), loginRequest.getSenha());
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Atualiza informações opcionais do usuário")
    @PutMapping("/{id}/optional")
    public ResponseEntity<UserResponseCadastroDTO> updateOptionalInfo(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequestOptionalDTO optionalDto) {
        var updatedUser = userService.updateOptionalInfo(id, optionalDto);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @Operation(summary = "Lista todos os usuários")
    @GetMapping
    public ResponseEntity<List<UserResponseCadastroDTO>> getAllUsers() {
        var users = userService.getListaUsuarios();
        return ResponseEntity.status(200).body(users);
    }

    @Operation(summary = "Busca usuário por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseCadastroDTO> getUserById(@PathVariable Integer id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Atualiza informações do usuário")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseCadastroDTO> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequestUpdateDTO updateDto) {
        var updatedUser = userService.updateUser(id, updateDto);
        return ResponseEntity.status(202).body(updatedUser);
    }

    @Operation(summary = "Exclui o usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Faz upload da imagem de perfil")
    @PostMapping("/{id}/imagem")
    public ResponseEntity<UserResponseCadastroDTO> uploadImagemPerfil(@PathVariable Integer id,
                                                                      @Valid @RequestBody UserRequestImagemPerfilDTO dto) {
        var updatedUser = userService.uploadImagemPerfil(id, dto);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @Operation(summary = "Atualiza a imagem de perfil")
    @PutMapping("/{id}/imagem")
    public ResponseEntity<UserResponseCadastroDTO> updateImagemPerfil(@PathVariable Integer id,
                                                                      @Valid @RequestBody UserRequestImagemPerfilDTO dto) {
        var updatedUser = userService.updateImagemPerfil(id, dto);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @Operation(summary = "Remove a imagem de perfil")
    @DeleteMapping("/{id}/imagem")
    public ResponseEntity<UserResponseCadastroDTO> removerImagemPerfil(@PathVariable Integer id) {
        var updatedUser = userService.deleteImagemPerfil(id);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @Operation(summary = "Retorna imagem por índice")
    @GetMapping("/{userId}/imagens/{indice}")
    public ResponseEntity<byte[]> getImagemPorIndice(@PathVariable Integer userId,
                                                     @PathVariable int indice) {
        byte[] dados = userService.getImagemPorIndice(userId, indice);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(dados, headers, HttpStatus.OK);
    }

    @Operation(summary = "Valida e-mail do usuário")
    @GetMapping("/{email}/validar-email")
    public ResponseEntity<UserResponseCadastroDTO> validarEmail(@PathVariable String email) {
        var user = userService.validarEmail(email);
        return ResponseEntity.status(200).body(user);
    }

    @Operation(summary = "Atualiza a senha do usuário")
    @PatchMapping("/senha")
    public ResponseEntity<UserResponseCadastroDTO> updateSenha(@Valid @RequestBody UserRequestSenhaDTO senha,
                                                               String email) {
        var updatedUser = userService.updateSenha(email, senha);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @DeleteMapping("/teste")
    public ResponseEntity<Void>deletarTodos(){
        userRepository.deleteAll();
        return ResponseEntity.status(204).build();
    }
}