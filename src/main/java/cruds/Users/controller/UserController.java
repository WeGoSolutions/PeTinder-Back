package cruds.Users.controller;

import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.request.UserRequestImagemPerfilDTO;
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
    public ResponseEntity<UserResponseLoginDTO> createUser(@Valid @RequestBody UserRequestCriarDTO userRequest) {
        var savedUser = userService.createUser(userRequest);
        return ResponseEntity.status(201).body(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseLoginDTO>> getAllUsers() {
        var users = userService.listarUsuarios();
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseLoginDTO> getUserById(@PathVariable Integer id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseLoginDTO> updateUser(@PathVariable Integer id, @Valid @RequestBody UserRequestCriarDTO userRequest) {
        var updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.status(202).body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{id}/imagem")
    public ResponseEntity<UserResponseLoginDTO> updateImagemPerfil(@PathVariable Integer id, @Valid @RequestBody UserRequestImagemPerfilDTO dto) {
        var updatedUser = userService.updateImagemPerfil(id, dto);
        return ResponseEntity.status(202).body(updatedUser);
    }
}