package cruds.Users.application.service;

import cruds.Users.application.service.interfaces.UserPasswordServiceInterface;
import cruds.Users.controller.dto.request.UserRequestSenhaDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.entity.User;
import cruds.Users.infrastructure.security.PasswordService;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.BadRequestException;
import cruds.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Serviço de aplicação responsável por operações relacionadas a senhas.
 * Centraliza mudanças de senha, reset e validações.
 */
@Service
@Transactional
public class UserPasswordService implements UserPasswordServiceInterface {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @Autowired
    public UserPasswordService(
            UserRepository userRepository,
            PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    /**
     * Atualiza a senha do usuário validando a senha atual
     */
    public UserResponseCadastroDTO updatePassword(UUID id, String senhaAtual, String novaSenha) {
        User user = findUserById(id);
        
        // Valida senha atual
        passwordService.validateCurrentPassword(senhaAtual, user.getSenha());
        
        // Prepara nova senha (valida e criptografa)
        String novaSenhaCriptografada = passwordService.prepareNewPassword(novaSenha);
        user.setSenha(novaSenhaCriptografada);
        
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    /**
     * Atualiza senha via email (processo de reset)
     */
    public UserResponseCadastroDTO updatePasswordByEmail(UserRequestSenhaDTO senhaDto) {
        validatePasswordResetRequest(senhaDto);
        
        User user = findUserByEmail(senhaDto.getEmail());
        
        // Prepara nova senha (valida e criptografa)
        String senhaCriptografada = passwordService.prepareNewPassword(senhaDto.getSenha());
        user.setSenha(senhaCriptografada);
        
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    private void validatePasswordResetRequest(UserRequestSenhaDTO senhaDto) {
        if (senhaDto.getEmail() == null || senhaDto.getEmail().isEmpty()) {
            throw new BadRequestException("Email é obrigatório para atualizar a senha.");
        }
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email " + email + " não encontrado"));
    }
}
