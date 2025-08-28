package cruds.Users.controller;

import cruds.Users.application.service.interfaces.UserManagementServiceInterface;
import cruds.Users.controller.dto.request.*;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller principal para usuários usando arquitetura orientada a objetos.
 * Refatorado para usar UserManagementServiceInterface que orquestra operações entre serviços especializados.
 * 
 * Benefícios da nova arquitetura:
 * - Controller mais limpo e focado apenas em HTTP
 * - Lógica de negócio separada em serviços especializados
 * - Uso de Domain Objects para encapsular regras de negócio
 * - Fácil manutenção e testes
 * - Princípios SOLID aplicados
 * - Desacoplamento através de interfaces
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Usuario", description = "Endpoints refatorados para gerenciamento de usuários com arquitetura POO.")
@Validated
public class UserController {

    private final UserManagementServiceInterface userManagementService;

    @Autowired
    public UserController(UserManagementServiceInterface userManagementService) {
        this.userManagementService = userManagementService;
    }

    @Operation(summary = "Cria um novo usuário", description = "Registra um novo usuário no sistema com validações automáticas de Value Objects")
    @PostMapping
    public ResponseEntity<UserResponseCadastroDTO> createUser(@Valid @RequestBody UserRequestCriarDTO userRequest) {
        var savedUser = userManagementService.createUser(userRequest);
        return ResponseEntity.status(201).body(savedUser);
    }

    @Operation(summary = "Realiza login do usuário", description = "Autentica usuário e retorna token JWT")
    @PostMapping("/login")
    public ResponseEntity<UserResponseLoginDTO> login(@RequestBody @Valid UserRequestLoginDTO loginDTO) {
        UserResponseLoginDTO response = userManagementService.login(loginDTO.getEmail(), loginDTO.getSenha());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualiza informações opcionais do usuário", description = "Permite atualização gradual do perfil")
    @PutMapping("/{id}/optional")
    public ResponseEntity<UserResponseCadastroDTO> updateOptionalInfo(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequestOptionalDTO optionalDto) {
        var updatedUser = userManagementService.updateOptionalInfo(id, optionalDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Lista todos os usuários", description = "Retorna lista de usuários usando Domain Objects")
    @GetMapping
    public ResponseEntity<List<UserResponseCadastroDTO>> getAllUsers() {
        var users = userManagementService.getListaUsuarios();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Busca usuário por ID", description = "Retorna dados do usuário usando Domain Object")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseCadastroDTO> getUserById(@PathVariable UUID id) {
        var user = userManagementService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Atualiza informações do usuário", description = "Atualização completa do perfil com validações de Value Objects")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseCadastroDTO> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequestUpdateDTO updateDto) {
        var updatedUser = userManagementService.updateUser(id, updateDto);
        return ResponseEntity.accepted().body(updatedUser);
    }

    @Operation(summary = "Exclui o usuário", description = "Remove usuário do sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userManagementService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza senha do usuário", description = "Permite alteração de senha com validação da senha atual")
    @PutMapping("/{id}/password")
    public ResponseEntity<UserResponseCadastroDTO> updatePassword(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequestUpdatePasswordDTO passwordDto) {
        var updatedUser = userManagementService.updatePassword(
            id, 
            passwordDto.getSenhaAtual(), 
            passwordDto.getNovaSenha()
        );
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Valida email do usuário", description = "Verifica se email existe no sistema")
    @GetMapping("/{email}/validar-email")
    public ResponseEntity<UserResponseCadastroDTO> validateEmailByPath(@PathVariable String email) {
        var user = userManagementService.validarEmail(email);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Valida email do usuário via query param", description = "Verifica se email existe no sistema")
    @GetMapping("/validate-email")
    public ResponseEntity<UserResponseCadastroDTO> validateEmail(@RequestParam String email) {
        var user = userManagementService.validarEmail(email);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Atualiza senha do usuário com segurança", description = "Endpoint alternativo para atualização de senha")
    @PatchMapping("/{id}/senha")
    public ResponseEntity<UserResponseCadastroDTO> updatePasswordSecure(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequestUpdatePasswordDTO passwordDto) {
        var updatedUser = userManagementService.updatePassword(
            id, 
            passwordDto.getSenhaAtual(), 
            passwordDto.getNovaSenha()
        );
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Atualiza senha via email", description = "Atualiza senha usando email como identificador")
    @PatchMapping("/senha")
    public ResponseEntity<UserResponseCadastroDTO> updatePasswordByEmail(
            @Valid @RequestBody UserRequestSenhaDTO senhaDto) {
        var updatedUser = userManagementService.updateSenha(senhaDto.getEmail(), senhaDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Marca usuário como não novo", description = "Atualiza status do usuário após completar perfil")
    @PutMapping("/{id}/mark-as-old")
    public ResponseEntity<UserResponseCadastroDTO> markUserAsNotNew(@PathVariable UUID id) {
        var updatedUser = userManagementService.atualizarUserNovoParaFalse(id);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Marca usuário como não novo (alternativo)", description = "Endpoint alternativo para atualizar status")
    @PatchMapping("/{id}/user-novo")
    public ResponseEntity<UserResponseCadastroDTO> updateUserNovoStatus(@PathVariable UUID id) {
        var updatedUser = userManagementService.atualizarUserNovoParaFalse(id);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Reset de senha por email", description = "Permite redefinição de senha usando email")
    @PutMapping("/reset-password")
    public ResponseEntity<UserResponseCadastroDTO> resetPassword(
            @Valid @RequestBody UserRequestSenhaDTO senhaDto) {
        var updatedUser = userManagementService.updateSenha(senhaDto.getEmail(), senhaDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Endpoint de teste para deletar todos os usuários", description = "⚠️ APENAS PARA DESENVOLVIMENTO")
    @DeleteMapping("/teste")
    public ResponseEntity<Void> deleteAllUsers() {
        // Este endpoint é apenas para desenvolvimento/testes
        // Na produção, deve ser removido por questões de segurança
        userManagementService.deleteAllUsers();
        return ResponseEntity.noContent().build();
    }

    // ========== ENDPOINTS DE IMAGEM (DEPRECATED - SERÃO MIGRADOS PARA PYTHON) ==========
    
    @Operation(summary = "Upload de imagem de perfil", description = "⚠️ DEPRECATED: Será migrado para microserviço Python")
    @PostMapping("/{id}/imagem")
    @Deprecated
    public ResponseEntity<UserResponseCadastroDTO> uploadImagemPerfil(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequestImagemPerfilDTO imagemDto) {
        var updatedUser = userManagementService.uploadImagemPerfil(id, imagemDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Atualiza imagem de perfil", description = "⚠️ DEPRECATED: Será migrado para microserviço Python")
    @PutMapping("/{id}/imagem")
    @Deprecated
    public ResponseEntity<UserResponseCadastroDTO> updateImagemPerfil(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequestImagemPerfilDTO imagemDto) {
        var updatedUser = userManagementService.updateImagemPerfil(id, imagemDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Busca imagem de perfil", description = "⚠️ DEPRECATED: Será migrado para microserviço Python")
    @GetMapping("/{id}/imagem")
    @Deprecated
    public ResponseEntity<String> getImagemPerfil(@PathVariable UUID id) {
        var imagemUrl = userManagementService.getUrlImageUser(id);
        return ResponseEntity.ok(imagemUrl.getUrl());
    }

    @Operation(summary = "Busca imagem específica por índice", description = "⚠️ DEPRECATED: Será migrado para microserviço Python")
    @GetMapping("/{id}/imagens/{indice}")
    @Deprecated
    public ResponseEntity<byte[]> getImagemPorIndice(
            @PathVariable UUID id,
            @PathVariable int indice) {
        byte[] imagem = userManagementService.getImagemPorIndice(id, indice);
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(imagem);
    }

    @Operation(summary = "Deleta imagem de perfil", description = "⚠️ DEPRECATED: Será migrado para microserviço Python")
    @DeleteMapping("/{id}/imagem")
    @Deprecated
    public ResponseEntity<UserResponseCadastroDTO> deleteImagemPerfil(@PathVariable UUID id) {
        var updatedUser = userManagementService.deleteImagemPerfil(id);
        return ResponseEntity.ok(updatedUser);
    }

    // Nota: Todos os endpoints de imagem acima estão marcados como @Deprecated
    // Estas funcionalidades serão migradas para um microserviço Python separado
    // seguindo o plano de reestruturação do projeto
}
