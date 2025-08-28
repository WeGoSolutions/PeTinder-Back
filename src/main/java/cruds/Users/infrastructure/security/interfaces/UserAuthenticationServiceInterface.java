package cruds.Users.infrastructure.security.interfaces;

import cruds.Users.controller.dto.request.UserRequestTokenDto;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.entity.User;

/**
 * Interface para serviço de autenticação de usuários.
 * Define contrato para operações de login e geração de tokens.
 */
public interface UserAuthenticationServiceInterface {
    
    /**
     * Realiza o login do usuário
     */
    UserResponseLoginDTO login(String email, String senha);
    
    /**
     * Autentica um usuário e retorna dados com token
     */
    UserRequestTokenDto autenticar(User usuario);
}
