package cruds.Users.application.service.interfaces;

import cruds.Users.controller.dto.request.*;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * Interface principal para gerenciamento de usuários.
 * Define o contrato público para operações de usuários.
 * 
 * Esta interface permite:
 * - Testabilidade através de mocks
 * - Inversão de dependência completa
 * - Flexibilidade para diferentes implementações
 * - Desacoplamento entre controller e implementação
 */
public interface UserManagementServiceInterface {

    // ===== OPERAÇÕES DE REGISTRO =====
    UserResponseCadastroDTO createUser(UserRequestCriarDTO dto);

    // ===== OPERAÇÕES DE AUTENTICAÇÃO =====
    UserResponseLoginDTO login(String email, String senha);
    UserRequestTokenDto autenticar(User usuario);

    // ===== OPERAÇÕES DE CONSULTA =====
    List<UserResponseCadastroDTO> getListaUsuarios();
    UserResponseCadastroDTO getUserById(UUID id);
    UserResponseCadastroDTO validarEmail(String email);

    // ===== OPERAÇÕES DE PERFIL =====
    UserResponseCadastroDTO updateOptionalInfo(UUID id, UserRequestOptionalDTO dto);
    UserResponseCadastroDTO updateUser(UUID id, UserRequestUpdateDTO dto);
    UserResponseCadastroDTO atualizarUserNovoParaFalse(UUID id);

    // ===== OPERAÇÕES DE SENHA =====
    UserResponseCadastroDTO updatePassword(UUID id, String senhaAtual, String novaSenha);
    UserResponseCadastroDTO updateSenha(String email, UserRequestSenhaDTO senhaDto);

    // ===== OPERAÇÕES DE EXCLUSÃO =====
    void deleteUser(UUID id);
}
