package cruds.Users.infrastructure.security;

import cruds.common.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por operações relacionadas a senhas.
 * Centraliza a lógica de criptografia e validação de senhas.
 */
@Service
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Criptografa uma senha
     */
    public String encryptPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new ConflictException("Senha não pode ser vazia para criptografia");
        }
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verifica se uma senha corresponde ao hash criptografado
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Valida se a senha atual está correta
     */
    public void validateCurrentPassword(String rawPassword, String encodedPassword) {
        if (!matches(rawPassword, encodedPassword)) {
            throw new ConflictException("Senha atual não confere");
        }
    }

    /**
     * Prepara uma nova senha (valida e criptografa)
     */
    public String prepareNewPassword(String rawPassword) {
        validatePasswordFormat(rawPassword);
        return encryptPassword(rawPassword);
    }

    private void validatePasswordFormat(String password) {
        if (password == null || password.isEmpty()) {
            throw new ConflictException("Senha é obrigatória");
        }
        if (password.length() < 8) {
            throw new ConflictException("Senha deve ter pelo menos 8 caracteres");
        }
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]+$")) {
            throw new ConflictException("Senha deve conter pelo menos: 1 letra maiúscula, 1 minúscula, 1 número e 1 caractere especial");
        }
    }
}
