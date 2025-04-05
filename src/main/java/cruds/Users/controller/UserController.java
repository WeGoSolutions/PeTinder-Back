package cruds.Users.controller;

import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.request.UserRequestImagemPerfilDTO;
import cruds.Users.controller.dto.request.UserRequestLoginDTO;
import cruds.Users.controller.dto.request.UserRequestOptionalDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseCadastroDTO> createUser(@Valid @RequestBody UserRequestCriarDTO userRequest) {
        var savedUser = userService.createUser(userRequest);
        return ResponseEntity.status(201).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseLoginDTO> login(@Valid @RequestBody UserRequestLoginDTO loginRequest) {
        var user = userService.login(loginRequest.getEmail(), loginRequest.getSenha());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/optional")
    public ResponseEntity<UserResponseCadastroDTO> updateOptionalInfo(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequestOptionalDTO optionalDto) {
        var updatedUser = userService.updateOptionalInfo(id, optionalDto);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseCadastroDTO>> getAllUsers() {
        var users = userService.getListaUsuarios();
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseCadastroDTO> getUserById(@PathVariable Integer id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseCadastroDTO> updateUser(@PathVariable Integer id, @Valid @RequestBody UserRequestCriarDTO userRequest) {
        var updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.status(202).body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("/{id}/imagem")
    public ResponseEntity<UserResponseCadastroDTO> uploadImagemPerfil(@PathVariable Integer id,
                                                                   @Valid @RequestBody UserRequestImagemPerfilDTO dto) {
        var updatedUser = userService.uploadImagemPerfil(id, dto);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @PutMapping("/{id}/imagem")
    public ResponseEntity<UserResponseCadastroDTO> updateImagemPerfil(@PathVariable Integer id,
                                                                   @Valid @RequestBody UserRequestImagemPerfilDTO dto) {
        var updatedUser = userService.updateImagemPerfil(id, dto);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @DeleteMapping("/{id}/imagem")
    public ResponseEntity<UserResponseCadastroDTO> removerImagemPerfil(@PathVariable Integer id) {
        var updatedUser = userService.deleteImagemPerfil(id);
        return ResponseEntity.status(200).body(updatedUser);
    }
}