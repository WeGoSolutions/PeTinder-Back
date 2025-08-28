package cruds.Users.application.facade;

import cruds.Users.application.service.interfaces.UserPasswordServiceInterface;
import cruds.Users.application.service.interfaces.UserProfileServiceInterface;
import cruds.Users.application.service.interfaces.UserQueryServiceInterface;
import cruds.Users.controller.dto.request.UserRequestOptionalDTO;
import cruds.Users.controller.dto.request.UserRequestSenhaDTO;
import cruds.Users.controller.dto.request.UserRequestUpdateDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.entity.User;
import cruds.Users.infrastructure.notification.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Facade responsável por operações de perfil do usuário.
 * Inclui atualização de dados pessoais e gerenciamento de senhas.
 */
@Service
@Transactional
public class UserProfileFacade {

    private final UserProfileServiceInterface userProfileService;
    private final UserPasswordServiceInterface userPasswordService;
    private final UserQueryServiceInterface userQueryService;
    private final UserNotificationService userNotificationService;

    @Autowired
    public UserProfileFacade(
            UserProfileServiceInterface userProfileService,
            UserPasswordServiceInterface userPasswordService,
            UserQueryServiceInterface userQueryService,
            UserNotificationService userNotificationService) {
        this.userProfileService = userProfileService;
        this.userPasswordService = userPasswordService;
        this.userQueryService = userQueryService;
        this.userNotificationService = userNotificationService;
    }

    /**
     * Atualiza informações opcionais do usuário
     */
    public UserResponseCadastroDTO updateOptionalInfo(UUID id, UserRequestOptionalDTO dto) {
        return userProfileService.updateOptionalInfo(id, dto);
    }

    /**
     * Atualiza informações completas do usuário
     */
    public UserResponseCadastroDTO updateUser(UUID id, UserRequestUpdateDTO dto) {
        return userProfileService.updateUser(id, dto);
    }

    /**
     * Marca usuário como não sendo mais novo
     */
    public UserResponseCadastroDTO atualizarUserNovoParaFalse(UUID id) {
        return userProfileService.markUserAsNotNew(id);
    }

    /**
     * Atualiza senha do usuário com notificação
     */
    public UserResponseCadastroDTO updatePassword(UUID id, String senhaAtual, String novaSenha) {
        UserResponseCadastroDTO response = userPasswordService.updatePassword(id, senhaAtual, novaSenha);
        
        // Enviar notificação de mudança de senha
        User user = userQueryService.findUserById(id);
        userNotificationService.sendPasswordResetNotification(user);
        
        return response;
    }

    /**
     * Atualiza senha via email com notificação
     */
    public UserResponseCadastroDTO updateSenha(String email, UserRequestSenhaDTO senhaDto) {
        UserResponseCadastroDTO response = userPasswordService.updatePasswordByEmail(senhaDto);
        
        // Enviar notificação de mudança de senha
        User user = userQueryService.findUserByEmail(email);
        userNotificationService.sendPasswordResetNotification(user);
        
        return response;
    }
}
