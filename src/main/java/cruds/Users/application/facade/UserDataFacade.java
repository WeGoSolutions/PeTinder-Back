package cruds.Users.application.facade;

import cruds.Users.application.service.interfaces.UserQueryServiceInterface;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Facade responsável por operações de consulta e validação de dados.
 * Centraliza todas as operações de leitura de usuários.
 */
@Service
@Transactional(readOnly = true)
public class UserDataFacade {

    private final UserQueryServiceInterface userQueryService;

    @Autowired
    public UserDataFacade(UserQueryServiceInterface userQueryService) {
        this.userQueryService = userQueryService;
    }

    /**
     * Lista todos os usuários
     */
    public List<UserResponseCadastroDTO> getListaUsuarios() {
        return userQueryService.getAllUsers();
    }

    /**
     * Busca usuário por ID
     */
    public UserResponseCadastroDTO getUserById(UUID id) {
        return userQueryService.getUserById(id);
    }

    /**
     * Valida email
     */
    public UserResponseCadastroDTO validarEmail(String email) {
        return userQueryService.validateEmail(email);
    }

    /**
     * Verifica se usuário existe
     */
    public boolean existsById(UUID id) {
        return userQueryService.existsById(id);
    }
}
