// src/test/java/cruds/Users/service/UserServiceTest.java
package cruds.Users.service;

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
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private EmailService emailService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Mock private ImageStorageStrategy imageStorageStrategy;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, eventPublisher, authenticationManager, gerenciadorTokenJwt, emailService);
        userService.imageStorageStrategy = imageStorageStrategy;
        user = new User();
        user.setId(1L);
        user.setEmail("test@user.com");
        user.setNome("Test User");
        user.setSenha("senhaCriptografada");
        user.setUserNovo(true);
    }

    @Test
    void createUser_success() {
        UserRequestCriarDTO dto = new UserRequestCriarDTO();
        dto.setEmail("novo@user.com");
        dto.setSenha("SenhaForte123!");
        dto.setNome("Novo User");
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(emailService).enviarEmail(anyString(), anyString(), anyString());

        var resp = userService.createUser(dto);
        assertEquals(dto.getEmail(), resp.getEmail());
        assertEquals(dto.getNome(), resp.getNome());
    }

    @Test
    void createUser_emailConflict() {
        UserRequestCriarDTO dto = new UserRequestCriarDTO();
        dto.setEmail("test@user.com");
        dto.setSenha("Senha1@");
        dto.setNome("Novo User");
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        assertThrows(ConflictException.class, () -> userService.createUser(dto));
    }

    @Test
    void createUser_senhaInvalida() {
        UserRequestCriarDTO dto = new UserRequestCriarDTO();
        dto.setEmail("novo@user.com");
        dto.setSenha("123");
        dto.setNome("Novo User");
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        assertThrows(ConflictException.class, () -> userService.createUser(dto));
    }

    @Test
    void updateOptionalInfo_success() {
        UserRequestOptionalDTO dto = new UserRequestOptionalDTO();
        dto.setCpf("123");
        dto.setCep("x");
        dto.setRua("r");
        dto.setNumero(1);
        dto.setComplemento("c");
        dto.setCidade("ct");
        dto.setUf("UF");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        var resp = userService.updateOptionalInfo(1, dto);
        assertEquals("123", resp.getCpf());
    }

    @Test
    void login_success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(gerenciadorTokenJwt.generateToken(any())).thenReturn("tok");
        doNothing().when(emailService).enviarEmail(anyString(), anyString(), anyString());
        var resp = userService.login(user.getEmail(), "senha");
        assertEquals(user.getEmail(), resp.getEmail());
        assertEquals("tok", resp.getToken());
    }

    @Test
    void login_emailNotFound() {
        when(userRepository.findByEmail("x@x.com")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.login("x@x.com", "senha"));
    }

    @Test
    void login_senhaInvalida() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> userService.login(user.getEmail(), "errada"));
    }

    @Test
    void getListaUsuarios_success() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        var resp = userService.getListaUsuarios();
        assertFalse(resp.isEmpty());
    }

    @Test
    void getListaUsuarios_noContent() {
        when(userRepository.findAll()).thenReturn(List.of());
        assertThrows(NoContentException.class, () -> userService.getListaUsuarios());
    }

    @Test
    void getUserById_success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        var resp = userService.getUserById(1);
        assertEquals(user.getEmail(), resp.getEmail());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUserById(2));
    }

    @Test
    void updatePassword_success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("novaEnc");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        var resp = userService.updatePassword(1, "senha", "novaSenha");
        assertEquals(user.getEmail(), resp.getEmail());
    }

    @Test
    void updatePassword_notFound() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.updatePassword(2, "a", "b"));
    }

    @Test
    void updatePassword_conflict() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        assertThrows(ConflictException.class, () -> userService.updatePassword(1, "errada", "nova"));
    }

    @Test
    void updateUser_success() {
        UserRequestUpdateDTO dto = new UserRequestUpdateDTO();
        dto.setNome("Novo");
        dto.setEmail("novo@x.com");
        dto.setDataNasc(LocalDate.now().minusYears(22));
        dto.setCpf("123");
        dto.setCep("x");
        dto.setRua("r");
        dto.setNumero(1);
        dto.setComplemento("c");
        dto.setCidade("ct");
        dto.setUf("UF");
        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        var resp = userService.updateUser(1, dto);
        assertEquals("Novo", resp.getNome());
    }

    @Test
    void updateUser_notFound() {
        UserRequestUpdateDTO dto = new UserRequestUpdateDTO();
        dto.setDataNasc(LocalDate.now().minusYears(22));
        when(userRepository.existsById(2)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> userService.updateUser(2, dto));
    }

    @Test
    void updateUser_notAllowed() {
        UserRequestUpdateDTO dto = new UserRequestUpdateDTO();
        dto.setDataNasc(LocalDate.now());
        when(userRepository.existsById(1)).thenReturn(true);
        assertThrows(NotAllowedException.class, () -> userService.updateUser(1, dto));
    }

    @Test
    void deleteUser_success() {
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);
        assertDoesNotThrow(() -> userService.deleteUser(1));
    }

    @Test
    void deleteUser_notFound() {
        when(userRepository.existsById(2)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> userService.deleteUser(2));
    }

    @Test
    void updateImagemPerfil_success() throws IOException {
        UserRequestImagemPerfilDTO dto = new UserRequestImagemPerfilDTO();
        byte[] imagem = new byte[]{1,2,3};
        dto.setImagemUsuario(Base64.getEncoder().encodeToString(imagem));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        doNothing().when(imageStorageStrategy).salvarImagem(any(), anyString());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Use o valor real da imagem no mock estático
        try (MockedStatic<ImageValidationUtil> util = Mockito.mockStatic(ImageValidationUtil.class)) {
            util.when(() -> ImageValidationUtil.validateUserImage(imagem, "perfil.jpg")).thenAnswer(inv -> null);
            var resp = userService.updateImagemPerfil(1, dto);
            assertEquals(user.getEmail(), resp.getEmail());
        }
    }

    @Test
    void uploadImagemPerfil_success() throws Exception {
        Long userId = 1L;
        byte[] imagemDecodificada = new byte[]{1, 2, 3};
        String userName = System.getProperty("user.name");
        String expectedCaminho = "C:\\Users\\" + userName + "/Desktop/S3 local/imagens/user_" + userId + "_perfil.jpg";

        User user = new User();
        user.setId(userId);

        UserRequestImagemPerfilDTO dto = mock(UserRequestImagemPerfilDTO.class);
        when(dto.getImagemDecodificada()).thenReturn(imagemDecodificada);

        when(userRepository.findById(Math.toIntExact(userId))).thenReturn(Optional.of(user));
        doNothing().when(imageStorageStrategy).salvarImagem(imagemDecodificada, expectedCaminho);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        try (MockedStatic<ImageValidationUtil> util = mockStatic(ImageValidationUtil.class)) {
            util.when(() -> ImageValidationUtil.validateUserImage(imagemDecodificada, "default.jpg")).thenAnswer(inv -> null);

            UserResponseCadastroDTO response = userService.uploadImagemPerfil(Math.toIntExact(userId), dto);

            assertNotNull(response);
            assertEquals(userId, response.getId());
            assertEquals(expectedCaminho, user.getImagemUser().getArquivo());

            verify(dto).getImagemDecodificada();
            verify(imageStorageStrategy).salvarImagem(imagemDecodificada, expectedCaminho);
        }
    }



    @Test
    void updateImagemPerfil_badRequest() throws IOException {
        UserRequestImagemPerfilDTO dto = new UserRequestImagemPerfilDTO();
        dto.setImagemUsuario(Base64.getEncoder().encodeToString(new byte[]{1,2,3}));
        try (MockedStatic<ImageValidationUtil> util = Mockito.mockStatic(ImageValidationUtil.class)) {
            util.when(() -> ImageValidationUtil.validateUserImage(any(), anyString())).thenThrow(new IOException("erro"));
            assertThrows(BadRequestException.class, () -> userService.updateImagemPerfil(1, dto));
        }
    }

    @Test
    void deleteImagemPerfil_noImage() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> userService.deleteImagemPerfil(1));
    }

    @Test
    void deleteImagemPerfil_success() {
        ImagemUser img = new ImagemUser(new byte[]{1});
        user.setImagemUser(img);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        var resp = userService.deleteImagemPerfil(1);
        assertNull(resp.getImagemUrl());
    }

    @Test
    void getImagemPorIndice_notFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        assertThrows(NotFoundException.class, () -> userService.getImagemPorIndice(1, 0));
    }

    @Test
    void getImagemPorIndice_wrongIndex() {
        ImagemUser img = new ImagemUser(new byte[]{1});
        user.setImagemUser(img);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        assertThrows(NotFoundException.class, () -> userService.getImagemPorIndice(1, 1));
    }

    @Test
    void getImagemPorIndice_success() {
        byte[] data = new byte[]{9};
        ImagemUser img = new ImagemUser(data);
        user.setImagemUser(img);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        var result = userService.getImagemPorIndice(1, 0);
        assertArrayEquals(data, result);
    }

    @Test
    void updateSenha_badRequest() {
        UserRequestSenhaDTO dto = new UserRequestSenhaDTO();
        dto.setSenha("x");
        dto.setEmail(null);
        assertThrows(BadRequestException.class, () -> userService.updateSenha("x", dto));
    }

    @Test
    void updateSenha_success() {
        UserRequestSenhaDTO dto = new UserRequestSenhaDTO();
        dto.setSenha("novaSenha");
        dto.setEmail(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        var resp = userService.updateSenha(user.getEmail(), dto);
        assertEquals(user.getEmail(), resp.getEmail());
    }

    @Test
    void validarEmail_success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        var resp = userService.validarEmail(user.getEmail());
        assertEquals(user.getEmail(), resp.getEmail());
    }

    @Test
    void validarEmail_notFound() {
        when(userRepository.findByEmail("x@x.com")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.validarEmail("x@x.com"));
    }

    @Test
    void atualizarUserNovoParaFalse_success() {
        user.setUserNovo(true);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        var resp = userService.atualizarUserNovoParaFalse(1);
        assertNotNull(resp);
    }

    @Test
    void autenticar_emailNotFound() {
        User u = new User();
        u.setEmail("x@y.com");
        u.setSenha("pass");
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(u.getEmail())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.autenticar(u));
    }

    @Test
    void autenticar_success() {
        User u = new User();
        u.setEmail("x@y.com");
        u.setSenha("pass");
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(userRepository.findByEmail(u.getEmail())).thenReturn(Optional.of(u));
        when(gerenciadorTokenJwt.generateToken(auth)).thenReturn("tok");
        var dto = userService.autenticar(u);
        assertEquals("tok", dto.getToken());
        assertEquals(u.getEmail(), dto.getEmail());
    }

    @Test
    void getUrlImageUser_success() {
        ImagemUser img = new ImagemUser(new byte[]{1});
        user.setImagemUser(img);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        var resp = userService.getUrlImageUser(1);
        assertEquals(user.getId(), resp.getId());
    }

    @Test
    void getUrlImageUser_conflict() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        assertThrows(ConflictException.class, () -> userService.getUrlImageUser(1));
    }
}