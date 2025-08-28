package cruds.Users.application.service.interfaces;

import cruds.Users.controller.dto.request.UserRequestSenhaDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;

import java.util.UUID;

/**
 * Interface para serviço de senhas de usuários.
 * Define contrato para operações relacionadas a senhas.
 */
public interface UserPasswordServiceInterface {
    
    /**
     * Atualiza a senha do usuário validando a senha atual
     */
    UserResponseCadastroDTO updatePassword(UUID id, String senhaAtual, String novaSenha);
    
    /**
     * Atualiza senha via email (processo de reset)
     */
    UserResponseCadastroDTO updatePasswordByEmail(UserRequestSenhaDTO senhaDto);
}
