package cruds.Users.infrastructure.security;

import cruds.Users.controller.UsuarioMapper;
import cruds.Users.controller.dto.request.UserRequestTokenDto;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.entity.User;
import cruds.Users.infrastructure.security.interfaces.UserAuthenticationServiceInterface;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.NotFoundException;
import cruds.config.token.GerenciadorTokenJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Serviço responsável por operações de autenticação e autorização.
 * Centraliza toda a lógica relacionada a login e geração de tokens.
 */
@Service
public class UserAuthenticationService implements UserAuthenticationServiceInterface {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final AuthenticationManager authenticationManager;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    public UserAuthenticationService(
            UserRepository userRepository,
            PasswordService passwordService,
            AuthenticationManager authenticationManager,
            GerenciadorTokenJwt gerenciadorTokenJwt) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.authenticationManager = authenticationManager;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
    }

    /**
     * Realiza o login do usuário
     */
    public UserResponseLoginDTO login(String email, String senha) {
        User user = findUserByEmail(email);
        validatePassword(senha, user.getSenha());
        
        String token = generateAuthenticationToken(email, senha);
        
        return UserResponseLoginDTO.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .token(token)
                .userNovo(user.getUserNovo())
                .build();
    }

    /**
     * Autentica um usuário e retorna dados com token
     */
    public UserRequestTokenDto autenticar(User usuario) {
        final UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        User usuarioAutenticado = userRepository.findByEmail(usuario.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Email do usuário não cadastrado"));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, token);
    }

    /**
     * Gera token de autenticação
     */
    private String generateAuthenticationToken(String email, String senha) {
        final UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(email, senha);
        final Authentication authentication = authenticationManager.authenticate(credentials);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return gerenciadorTokenJwt.generateToken(authentication);
    }

    /**
     * Busca usuário por email
     */
    private User findUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Email não encontrado");
        }
        return userOptional.get();
    }

    /**
     * Valida se a senha está correta
     */
    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordService.matches(rawPassword, encodedPassword)) {
            throw new NotFoundException("Senha inválida");
        }
    }
}
