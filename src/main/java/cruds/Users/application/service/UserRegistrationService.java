package cruds.Users.application.service;

import cruds.Users.application.dto.mapper.UserDomainMapper;
import cruds.Users.application.service.interfaces.UserRegistrationServiceInterface;
import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.domain.factory.UserDomainFactory;
import cruds.Users.domain.model.UserDomain;
import cruds.Users.domain.service.UserDomainService;
import cruds.Users.entity.User;
import cruds.Users.infrastructure.notification.UserNotificationService;
import cruds.Users.infrastructure.security.PasswordService;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço de aplicação responsável pelo fluxo de registro de usuários.
 * Refatorado para usar arquitetura baseada em POO com UserDomain.
 */
@Service
@Transactional
public class UserRegistrationService implements UserRegistrationServiceInterface {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final UserDomainFactory userDomainFactory;
    private final PasswordService passwordService;
    private final UserNotificationService userNotificationService;
    private final UserDomainMapper userDomainMapper;

    @Autowired
    public UserRegistrationService(
            UserRepository userRepository,
            UserDomainService userDomainService,
            UserDomainFactory userDomainFactory,
            PasswordService passwordService,
            UserNotificationService userNotificationService,
            UserDomainMapper userDomainMapper) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
        this.userDomainFactory = userDomainFactory;
        this.passwordService = passwordService;
        this.userNotificationService = userNotificationService;
        this.userDomainMapper = userDomainMapper;
    }

    /**
     * Registra um novo usuário no sistema usando arquitetura orientada a objetos
     */
    public UserResponseCadastroDTO registerUser(UserRequestCriarDTO dto) {
        // Converter DTO para dados de criação
        UserDomainMapper.UserCreationData creationData = userDomainMapper.fromCreationDto(dto);
        
        // Validar unicidade do email
        validateEmailUniqueness(creationData.getEmail());
        
        // Criar domain object com validações automáticas
        UserDomain userDomain = userDomainService.validateAndCreateUser(
            creationData.getName(),
            creationData.getEmail(),
            creationData.getPassword(),
            creationData.getBirthDate()
        );
        
        // Criptografar e definir senha
        String encryptedPassword = passwordService.encryptPassword(creationData.getPassword());
        userDomain.setEncryptedPassword(encryptedPassword);
        
        // Converter para entidade e salvar
        User userEntity = userDomainFactory.toEntity(userDomain);
        User savedUser = userRepository.save(userEntity);
        
        // Atualizar domain com ID gerado
        UserDomain savedUserDomain = userDomainFactory.fromEntity(savedUser);
        
        // Enviar notificação de boas-vindas
        userNotificationService.sendWelcomeEmail(savedUser);
        
        // Converter para DTO de resposta
        return userDomainMapper.toResponseDto(savedUserDomain);
    }

    /**
     * Valida se o email é único no sistema
     */
    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Email já está em uso");
        }
    }
}
