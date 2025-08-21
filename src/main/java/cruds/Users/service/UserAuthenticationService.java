package cruds.Users.service;

import cruds.Users.controller.dto.request.UserRequestUpdatePasswordDTO;
import cruds.Users.entity.User;
import cruds.Users.mapper.UserMapper;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.ConflictException;
import cruds.common.exception.NotFoundException;
import cruds.config.token.GerenciadorTokenJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final GerenciadorTokenJwt gerenciadorTokenJwt;
  private final UserMapper userMapper;
  private final UserQueryService userQueryService;

  public String login(String email, String senha) {
    User user = userQueryService.findUserByEmail(email);

    if (!passwordEncoder.matches(senha, user.getSenha())) {
      throw new NotFoundException("Email ou senha inválidos");
    }

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, senha)
    );

    // Disparar evento de notificação de login
    return gerenciadorTokenJwt.generateToken(authentication);
  }

  @Transactional
  public void updatePassword(Integer id, UserRequestUpdatePasswordDTO dto) {
    User user = userQueryService.findUserById(id);

    if (!passwordEncoder.matches(dto.getSenhaAtual(), user.getSenha())) {
      throw new ConflictException("Senha atual não confere");
    }

    user.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
    userRepository.save(user);
  }
}