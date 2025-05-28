package cruds.Users.service;

import cruds.Users.controller.UsuarioMapper;
import cruds.Users.controller.dto.request.*;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.controller.dto.response.UserResponseUrlDTO;
import cruds.Users.entity.Endereco;
import cruds.Users.entity.ImagemUser;
import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.*;
import cruds.common.service.EmailService;
import cruds.common.strategy.ImageStorageStrategy;
import cruds.common.util.ImageValidationUtil;
import cruds.config.token.GerenciadorTokenJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final EmailService emailService;
    private AuthenticationManager authenticationManager;
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    private static final String DEFAULT_IMAGE_NAME = "perfil.jpg"; //alterar para a imagem default
    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Desktop/S3 local/imagens/";

    @Autowired
    @Qualifier("localImageStorageStrategy")
    private ImageStorageStrategy imageStorageStrategy;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher, AuthenticationManager authenticationManager, GerenciadorTokenJwt gerenciadorTokenJwt, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.authenticationManager = authenticationManager;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.emailService = emailService;
    }

    public UserResponseCadastroDTO createUser(UserRequestCriarDTO dto) {
        userRules(dto);
        User user = UserRequestCriarDTO.toEntity(dto);
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        user.setSenha(senhaCriptografada);
        User savedUser = userRepository.save(user);
        emailService.enviarEmail(savedUser.getEmail(),
                "Bem-vindo ao PeTinder, %s!".formatted(user.getNome()),
                """
                            <div style="font-family: Arial, sans-serif; background-color: #fefefe; padding: 20px; border-radius: 10px; border: 1px solid #ddd;">
                                <h1 style="color: #ff6f61;">🐾 Bem-vindo ao PeTinder, %s!</h1>
                        
                                <p style="font-size: 16px; color: #333;">
                                    Estamos super felizes por ter você com a gente! <br>
                                    Aqui no <strong>PeTinder</strong>, acreditamos que todo pet merece um lar cheio de amor, e toda pessoa merece um pet que mude sua vida. 💕
                                </p>
                        
                                <p style="font-size: 16px; color: #333;">
                                    Prepare-se para conhecer novos amigos peludos, descobrir histórias emocionantes e, quem sabe, encontrar seu novo companheiro de quatro patas.
                                </p>
                        
                                <p style="font-size: 14px; color: #666;">Com carinho,<br>Equipe PeTinder 🐶🐱</p>
                            </div>
                        """.formatted(user.getNome()));
        return UserResponseCadastroDTO.toResponse(savedUser);
    }

    public UserResponseCadastroDTO updateOptionalInfo(Integer id, UserRequestOptionalDTO dto) {
        User user = getUsuarioPorId(id);

        if (dto.getCpf() != null) {
            user.setCpf(dto.getCpf());
        }

        Endereco endereco = (user.getEndereco() != null) ? user.getEndereco() : new Endereco();
        endereco.setCep(dto.getCep());
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setCidade(dto.getCidade());
        endereco.setUf(dto.getUf());
        user.setEndereco(endereco);

        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public UserResponseLoginDTO login(String email, String senha) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Email não encontrado");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new NotFoundException("Senha inválida");
        }

        final UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(email, senha);
        final Authentication authentication = authenticationManager.authenticate(credentials);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = gerenciadorTokenJwt.generateToken(authentication);

        LocalDateTime loginTime = LocalDateTime.now();

        emailService.enviarEmail(
                user.getEmail(),
                "🔒 Novo login no PeTinder, " + user.getNome() + "!",
                """
                        <div style="font-family: Arial, sans-serif; background-color: #ffffff; padding: 20px; border-radius: 8px; border:1px solid #e0e0e0;">
                          <h2 style="color: #4a90e2;">🔒 Olá, %s!</h2>
                          <p style="font-size: 16px; color: #333;">
                            Detectamos um <strong>novo acesso</strong> à sua conta em <em>%s</em>.
                          </p>
                          <p style="font-size: 15px; color: #333;">
                            Se foi você, continue aproveitando o PeTinder. 😊<br>
                            Caso não reconheça este acesso, <strong>recomendamos</strong> trocar sua senha imediatamente.
                          </p>
                          <p style="font-size: 14px; color: #777;">
                            Abraços,<br>
                            Equipe PeTinder 🐶🐱
                          </p>
                        </div>
                        """.formatted(
                        user.getNome(),
                        loginTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"))
                )
        );

        return UserResponseLoginDTO.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .token(token)
                .userNovo(user.getUserNovo())
                .build();
    }

    public List<UserResponseCadastroDTO> getListaUsuarios() {
        var usuarios = userRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new NoContentException("Nenhum usuário encontrado");
        }
        return usuarios.stream()
                .map(UserResponseCadastroDTO::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponseCadastroDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id: " + id + " não encontrado"));
        return UserResponseCadastroDTO.toResponse(user);
    }

    public UserResponseCadastroDTO updatePassword(Integer id, String senhaAtual, String novaSenha) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User não encontrado"));

        if (!passwordEncoder.matches(senhaAtual, user.getSenha())) {
            throw new ConflictException("Senha atual não confere");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(novaSenha);
        user.setSenha(novaSenhaCriptografada);
        userRepository.save(user);

        return UserResponseCadastroDTO.toResponse(user);
    }

    public UserResponseCadastroDTO updateUser(Integer id, UserRequestUpdateDTO dto) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuário com id: " + id + " não encontrado");
        }
        if (!dto.isMaiorDe21()) {
            throw new NotAllowedException("A pessoa deve ter mais de 21 anos");
        }

        User user = getUsuarioPorId(id);
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setDataNasc(dto.getDataNasc());
        user.setCpf(dto.getCpf());

        Endereco endereco = (user.getEndereco() != null) ? user.getEndereco() : new Endereco();
        endereco.setCep(dto.getCep());
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setCidade(dto.getCidade());
        endereco.setUf(dto.getUf());
        user.setEndereco(endereco);

        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuário com id: " + id + " não encontrado");
        }
        userRepository.deleteById(id);
    }

    public UserResponseCadastroDTO updateImagemPerfil(Integer id, UserRequestImagemPerfilDTO dto) {
        byte[] imagemDecodificada = dto.getImagemDecodificada();
        try {
            ImageValidationUtil.validateUserImage(imagemDecodificada, DEFAULT_IMAGE_NAME);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao processar a imagem: " + e.getMessage());
        }

        User user = getUsuarioPorId(id);
        String nomeArquivo = "user_" + id + "_perfil.jpg";
        String caminhoCompleto = UPLOAD_DIR + nomeArquivo;
        try {
            imageStorageStrategy.salvarImagem(imagemDecodificada, caminhoCompleto);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao salvar a imagem: " + e.getMessage());
        }
        if (user.getImagemUser() != null) {
            user.getImagemUser().setDados(imagemDecodificada);
            user.getImagemUser().setArquivo(caminhoCompleto);
        } else {
            ImagemUser imagemUser = new ImagemUser(imagemDecodificada);
            imagemUser.setArquivo(caminhoCompleto);
            user.setImagemUser(imagemUser);
        }
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public UserResponseCadastroDTO uploadImagemPerfil(Integer id, UserRequestImagemPerfilDTO dto) {
        byte[] imagemDecodificada = dto.getImagemDecodificada();
        try {
            ImageValidationUtil.validateUserImage(imagemDecodificada, DEFAULT_IMAGE_NAME);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao processar a imagem: " + e.getMessage());
        }

        User user = getUsuarioPorId(id);
        String nomeArquivo = "user_" + id + "_perfil.jpg";
        String caminhoCompleto = UPLOAD_DIR + nomeArquivo;
        try {
            imageStorageStrategy.salvarImagem(imagemDecodificada, caminhoCompleto);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao salvar a imagem: " + e.getMessage());
        }
        ImagemUser imagemUser = new ImagemUser(imagemDecodificada);
        imagemUser.setArquivo(caminhoCompleto);
        user.setImagemUser(imagemUser);
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public UserResponseCadastroDTO deleteImagemPerfil(Integer id) {
        User user = getUsuarioPorId(id);
        if (user.getImagemUser() == null) {
            throw new BadRequestException("Usuário não possui uma imagem de perfil para remover.");
        }
        user.setImagemUser(null);
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public byte[] getImagemPorIndice(Integer userId, int indice) {
        User user = getUsuarioPorId(userId);
        if (user.getImagemUser() == null || indice != 0) {
            throw new NotFoundException("Imagem não encontrada para o usuário com id " + userId);
        }
        return user.getImagemUser().getDados();
    }

    public UserResponseCadastroDTO updateSenha(String email, UserRequestSenhaDTO senhaDto) {
        if (senhaDto.getEmail() == null || senhaDto.getEmail().isEmpty()) {
            throw new BadRequestException("Email é obrigatório para atualizar a senha.");
        }
        User user = getUsuarioPorEmail(senhaDto.getEmail());
        String senhaCriptografada = passwordEncoder.encode(senhaDto.getSenha());
        user.setSenha(senhaCriptografada);
        return UserResponseCadastroDTO.toResponse(userRepository.save(user));
    }

    public UserResponseCadastroDTO validarEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email " + email + " não encontrado"));
        return UserResponseCadastroDTO.toResponse(user);
    }

    public UserResponseCadastroDTO atualizarUserNovoParaFalse(Integer id) {
        User user = getUsuarioPorId(id);
        user.setUserNovo(false);
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    private User getUsuarioPorId(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id " + id + " não encontrado"));
    }

    private User getUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email " + email + " não encontrado"));
    }

    private void userRules(UserRequestCriarDTO user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@") || userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ConflictException("Email não pode ser utilizado");
        }
        if (user.getSenha() == null || user.getSenha().isEmpty() || user.getSenha().length() < 8
                || !user.getSenha().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$")) {
            throw new ConflictException("Senha não pode ser cadastrada ou não atende aos requisitos de segurança");
        }
        if (user.getNome() == null || user.getNome().isEmpty() || user.getNome().length() < 3
                || !user.getNome().matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) {
            throw new ConflictException("Nome não pode ser utilizado");
        }

        user.isMaiorDe21();
    }

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

    public UserResponseUrlDTO getUrlImageUser(Integer id) {
        User user = getUsuarioPorId(id);
        if (user.getImagemUser() == null) {
            throw new ConflictException("Imagem não encontrada");
        }
        return UserResponseUrlDTO.toResponse(user);
    }
}

