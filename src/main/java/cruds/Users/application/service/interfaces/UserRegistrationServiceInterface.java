package cruds.Users.application.service.interfaces;

import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;

/**
 * Interface para serviço de registro de usuários.
 * Define contrato para operações de cadastro e validações relacionadas.
 */
public interface UserRegistrationServiceInterface {
    
    /**
     * Registra um novo usuário no sistema
     */
    UserResponseCadastroDTO registerUser(UserRequestCriarDTO dto);
}
