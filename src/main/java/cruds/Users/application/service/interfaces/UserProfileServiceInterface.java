package cruds.Users.application.service.interfaces;

import cruds.Users.controller.dto.request.UserRequestOptionalDTO;
import cruds.Users.controller.dto.request.UserRequestUpdateDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;

import java.util.UUID;

/**
 * Interface para serviço de perfil de usuários.
 * Define contrato para operações de atualização de perfil.
 */
public interface UserProfileServiceInterface {
    
    /**
     * Atualiza informações opcionais do usuário
     */
    UserResponseCadastroDTO updateOptionalInfo(UUID id, UserRequestOptionalDTO dto);
    
    /**
     * Atualiza informações completas do usuário
     */
    UserResponseCadastroDTO updateUser(UUID id, UserRequestUpdateDTO dto);
    
    /**
     * Marca o usuário como não sendo mais novo
     */
    UserResponseCadastroDTO markUserAsNotNew(UUID id);
    
    /**
     * Método de compatibilidade - delega para markUserAsNotNew
     * @deprecated Use markUserAsNotNew em vez deste método
     */
    @Deprecated
    UserResponseCadastroDTO atualizarUserNovoParaFalse(UUID id);
}
