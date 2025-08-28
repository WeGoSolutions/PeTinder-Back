package cruds.Users.application.facade;

import cruds.Users.application.service.interfaces.UserAuthenticationServiceInterface;
import cruds.Users.application.service.interfaces.UserQueryServiceInterface;
import cruds.Users.application.service.interfaces.UserRegistrationServiceInterface;
import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.request.UserRequestTokenDto;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.entity.User;
import cruds.Users.infrastructure.notification.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade responsável por operações de conta do usuário (registro e autenticação).
 * Simplifica a complexidade do UserManagementService agrupando operações relacionadas.
 */
@Service
@Transactional
public class UserAccountFacade {

    private final UserRegistrationServiceInterface userRegistrationService;
    private final UserAuthenticationServiceInterface userAuthenticationService;
    private final UserQueryServiceInterface userQueryService;
    private final UserNotificationService userNotificationService;

    @Autowired
    public UserAccountFacade(
            UserRegistrationServiceInterface userRegistrationService,
            UserAuthenticationServiceInterface userAuthenticationService,
            UserQueryServiceInterface userQueryService,
            UserNotificationService userNotificationService) {
        this.userRegistrationService = userRegistrationService;
        this.userAuthenticationService = userAuthenticationService;
        this.userQueryService = userQueryService;
        this.userNotificationService = userNotificationService;
    }

    /**
     * Registra um novo usuário no sistema
     */
    public UserResponseCadastroDTO createUser(UserRequestCriarDTO dto) {
        return userRegistrationService.registerUser(dto);
    }

    /**
     * Realiza login do usuário com notificação automática
     */
    public UserResponseLoginDTO login(String email, String senha) {
        UserResponseLoginDTO response = userAuthenticationService.login(email, senha);
        
        // Buscar usuário para enviar notificação
        User user = userQueryService.findUserByEmail(email);
        userNotificationService.sendLoginNotification(user);
        
        return response;
    }

    /**
     * Autentica usuário e retorna token
     */
    public UserRequestTokenDto autenticar(User usuario) {
        return userAuthenticationService.autenticar(usuario);
    }
}
