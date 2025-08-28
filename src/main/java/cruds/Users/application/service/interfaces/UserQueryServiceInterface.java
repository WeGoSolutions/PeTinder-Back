package cruds.Users.application.service.interfaces;

import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.domain.model.UserDomain;
import cruds.Users.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * Interface para serviço de consulta de usuários.
 * Define contrato para operações de busca e validação.
 */
public interface UserQueryServiceInterface {
    
    /**
     * Lista todos os usuários do sistema
     */
    List<UserResponseCadastroDTO> getAllUsers();
    
    /**
     * Busca usuário por ID retornando DTO
     */
    UserResponseCadastroDTO getUserById(UUID id);
    
    /**
     * Valida email e retorna dados do usuário
     */
    UserResponseCadastroDTO validateEmail(String email);
    
    /**
     * Busca usuário por email retornando domain object
     */
    UserDomain findUserDomainByEmail(String email);
    
    /**
     * Busca usuário por ID retornando domain object
     */
    UserDomain findUserDomainById(UUID id);
    
    /**
     * Busca usuário por email (entidade JPA)
     */
    User findUserByEmail(String email);
    
    /**
     * Busca usuário por ID (entidade JPA)
     */
    User findUserById(UUID id);
    
    /**
     * Verifica se um usuário existe por ID
     */
    boolean existsById(UUID id);
}
