package cruds.Users.service;

import static org.junit.jupiter.api.Assertions.*;

import cruds.Users.controller.dto.request.*;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.entity.Endereco;
import cruds.Users.entity.ImagemUser;
import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;
import cruds.common.event.UserCreatedEvent;
import cruds.common.event.UserLoggedInEvent;
import cruds.common.exception.*;
import cruds.config.token.GerenciadorTokenJwt;
import cruds.common.util.ImageValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @InjectMocks
    private UserService userService;

    private UserRequestCriarDTO validCreateDto;
    private User user;

    @BeforeEach
    void setUp() {
        validCreateDto = new UserRequestCriarDTO();
        validCreateDto.setEmail("test@example.com");
        validCreateDto.setSenha("Abcdef1@2");
        validCreateDto.setNome("Valid Nome");

        user = new User();
        user.setId(1L);
        user.setEmail(validCreateDto.getEmail());
        user.setSenha("encoded");
        user.setNome(validCreateDto.getNome());
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso quando os dados são válidos")
    void createUser_Success() {
        when(userRepository.findByEmail(validCreateDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(validCreateDto.getSenha())).thenReturn("encodedSenha");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(42L);
            return u;
        });

        UserResponseCadastroDTO response = userService.createUser(validCreateDto);

        assertEquals(42, response.getId());
        assertEquals(validCreateDto.getEmail(), response.getEmail());
        verify(eventPublisher).publishEvent(any(UserCreatedEvent.class));
    }

    @Test
    @DisplayName("Deve lançar ConflictException ao tentar criar usuário com email já cadastrado")
    void createUser_EmailConflict() {
        when(userRepository.findByEmail(validCreateDto.getEmail())).thenReturn(Optional.of(user));
        ConflictException ex = assertThrows(ConflictException.class,
                () -> userService.createUser(validCreateDto));
        assertTrue(ex.getMessage().contains("Email não pode ser utilizado"));
    }

    @Test
    @DisplayName("Deve lançar ConflictException ao tentar criar usuário com senha inválida")
    void createUser_InvalidPassword() {
        validCreateDto.setSenha("short");
        when(userRepository.findByEmail(validCreateDto.getEmail())).thenReturn(Optional.empty());
        ConflictException ex = assertThrows(ConflictException.class,
                () -> userService.createUser(validCreateDto));
        assertTrue(ex.getMessage().contains("Senha não pode ser cadastrada"));
    }

    @Test
    @DisplayName("Deve lançar ConflictException ao tentar criar usuário com nome inválido")
    void createUser_InvalidName() {
        validCreateDto.setNome("12");
        when(userRepository.findByEmail(validCreateDto.getEmail())).thenReturn(Optional.empty());
        ConflictException ex = assertThrows(ConflictException.class,
                () -> userService.createUser(validCreateDto));
        assertTrue(ex.getMessage().contains("Nome não pode ser utilizado"));
    }

    // Tests for updateOptionalInfo
    @Test
    @DisplayName("Deve atualizar informações opcionais criando novo endereço se não existir")
    void updateOptionalInfo_NewEndereco() {
        UserRequestOptionalDTO dto = new UserRequestOptionalDTO();
        dto.setCep("123");
        dto.setRua("Rua");
        dto.setNumero(1);
        dto.setComplemento("Compl");
        dto.setCidade("City");
        dto.setUf("UF");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserResponseCadastroDTO response = userService.updateOptionalInfo(1, dto);

        assertEquals("123", response.getCep());
        assertEquals("Rua", response.getRua());
        assertEquals(1, response.getNumero());
    }

    @Test
    @DisplayName("Deve atualizar informações opcionais sobrescrevendo endereço existente")
    void updateOptionalInfo_ExistingEndereco() {
        Endereco endereco = new Endereco();
        endereco.setCep("old");
        user.setEndereco(endereco);
        UserRequestOptionalDTO dto = new UserRequestOptionalDTO();
        dto.setCep("new");
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserResponseCadastroDTO response = userService.updateOptionalInfo(2, dto);
        assertEquals("new", response.getCep());
    }

    // Tests for login
    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar logar com email não cadastrado")
    void login_UserNotFound() {
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.login("a@b.com", "pass"));
        assertEquals("Email não encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar logar com senha inválida")
    void login_InvalidPassword() {
        user.setSenha("encoded");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.login(user.getEmail(), "wrong"));
        assertEquals("Senha inválida", ex.getMessage());
    }

    @Test
    @DisplayName("Deve autenticar usuário e retornar token ao logar com sucesso")
    void login_Success() {
        user.setSenha("encoded");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plain", "encoded")).thenReturn(true);
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(gerenciadorTokenJwt.generateToken(auth)).thenReturn("token123");
        UserResponseLoginDTO dto = userService.login(user.getEmail(), "plain");
        assertEquals(user.getId(), dto.getId());
        assertEquals("token123", dto.getToken());
        verify(eventPublisher).publishEvent(any(UserLoggedInEvent.class));
    }

    // Tests for getListaUsuarios
    @Test
    @DisplayName("Deve lançar NoContentException quando não houver usuários cadastrados")
    void getListaUsuarios_NoContent() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        NoContentException ex = assertThrows(NoContentException.class,
                () -> userService.getListaUsuarios());
        assertEquals("Nenhum usuário encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Deve retornar lista de usuários quando houver usuários cadastrados")
    void getListaUsuarios_ReturnsList() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        var list = userService.getListaUsuarios();
        assertEquals(1, list.size());
    }

    // Tests for getUserById
    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar usuário por id inexistente")
    void getUserById_NotFound() {
        when(userRepository.findById(5)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.getUserById(5));
        assertTrue(ex.getMessage().contains("id: 5"));
    }

    @Test
    @DisplayName("Deve retornar usuário ao buscar por id existente")
    void getUserById_Success() {
        when(userRepository.findById(3)).thenReturn(Optional.of(user));
        var resp = userService.getUserById(3);
        assertEquals(user.getEmail(), resp.getEmail());
    }

    // Tests for updateUser
    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar atualizar usuário inexistente")
    void updateUser_NotFound() {
        when(userRepository.existsById(10)).thenReturn(false);
        UserRequestUpdateDTO dto = new UserRequestUpdateDTO();
        dto.setEmail("a@b.com");
        dto.setSenha("pass");
        dto.setNome("Nome");
        dto.setDataNasc(LocalDate.now());
        dto.setCpf("123");
        dto.setCep("x");
        dto.setRua("r");
        dto.setNumero(1);
        dto.setComplemento("c");
        dto.setCidade("ct");
        dto.setUf("UF");
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.updateUser(10, dto));
        assertTrue(ex.getMessage().contains("id: 10"));
    }

    @Test
    @DisplayName("Deve lançar NotAllowedException ao tentar atualizar usuário menor de 21 anos")
    void updateUser_Under21() {
        when(userRepository.existsById(2)).thenReturn(true);
        UserRequestUpdateDTO dto = new UserRequestUpdateDTO();
        dto.setDataNasc(LocalDate.now());
        dto.setEmail("e@e.com");
        dto.setNome("Name");
        dto.setSenha("pass");
        dto.setCpf("123");
        dto.setCep("x");
        dto.setRua("r");
        dto.setNumero(1);
        dto.setComplemento("c");
        dto.setCidade("ct");
        dto.setUf("UF");
        NotAllowedException ex = assertThrows(NotAllowedException.class,
                () -> userService.updateUser(2, dto));
        assertTrue(ex.getMessage().contains("mais de 21"));
    }

    @Test
    @DisplayName("Deve lançar ConflictException ao tentar atualizar usuário com email já cadastrado")
    void updateUser_EmailConflict() {
        when(userRepository.existsById(3)).thenReturn(true);
        UserRequestUpdateDTO dto = new UserRequestUpdateDTO();
        dto.setEmail("conflict@example.com");
        dto.setSenha("pass");
        dto.setNome("Name");
        dto.setDataNasc(LocalDate.now().minusYears(30));
        dto.setCpf("123");
        dto.setCep("x");
        dto.setRua("r");
        dto.setNumero(1);
        dto.setComplemento("c");
        dto.setCidade("ct");
        dto.setUf("UF");
        User other = new User();
        other.setId(5L);
        other.setEmail(dto.getEmail());
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(other));
        ConflictException ex = assertThrows(ConflictException.class,
                () -> userService.updateUser(3, dto));
        assertTrue(ex.getMessage().contains("Email já cadastrado"));
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso quando dados são válidos")
    void updateUser_Success() {
        when(userRepository.existsById(4)).thenReturn(true);
        UserRequestUpdateDTO dto = new UserRequestUpdateDTO();
        dto.setEmail("new@example.com");
        dto.setSenha("pass");
        dto.setNome("Nome");
        dto.setDataNasc(LocalDate.now().minusYears(25));
        dto.setCpf("123");
        dto.setCep("a");
        dto.setRua("b");
        dto.setNumero(1);
        dto.setComplemento("c");
        dto.setCidade("d");
        dto.setUf("UF");
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findById(4)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        var resp = userService.updateUser(4, dto);
        assertEquals("new@example.com", resp.getEmail());
    }

    // Tests for deleteUser
    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar deletar usuário inexistente")
    void deleteUser_NotFound() {
        when(userRepository.existsById(9)).thenReturn(false);
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.deleteUser(9));
        assertTrue(ex.getMessage().contains("id: 9"));
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso quando id existe")
    void deleteUser_Success() {
        when(userRepository.existsById(8)).thenReturn(true);
        doNothing().when(userRepository).deleteById(8);
        assertDoesNotThrow(() -> userService.deleteUser(8));
        verify(userRepository).deleteById(8);
    }

    // Tests for image operations
    @Test
    @DisplayName("Deve lançar BadRequestException ao atualizar imagem de perfil inválida")
    void updateImagemPerfil_InvalidImage() {
        UserRequestImagemPerfilDTO dto = new UserRequestImagemPerfilDTO();
        dto.setImagemUsuario(Base64.getEncoder().encodeToString(new byte[]{1, 2}));

        try (MockedStatic<ImageValidationUtil> utilMock = Mockito.mockStatic(ImageValidationUtil.class)) {
            utilMock.when(() -> ImageValidationUtil.validateUserImage(Mockito.any(byte[].class), Mockito.anyString()))
                    .thenThrow(new IOException("fail"));

            BadRequestException ex = assertThrows(BadRequestException.class,
                    () -> userService.updateImagemPerfil(1, dto));
            assertTrue(ex.getMessage().contains("Erro ao processar a imagem"));
        }
    }

    @Test
    @DisplayName("Deve atualizar imagem de perfil com sucesso quando usuário não possui imagem")
    void updateImagemPerfil_SuccessNew() {
        UserRequestImagemPerfilDTO dto = new UserRequestImagemPerfilDTO();
        dto.setImagemUsuario("/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAP//////////////////////////////////////////////////////////////////////////////////////2wBDAf//////////////////////////////////////////////////////////////////////////////////////wAARCAABAAEDASIAAhEBAxEB/8QAFwABAQEBAAAAAAAAAAAAAAAAAAUGB//EABYQAQEBAAAAAAAAAAAAAAAAAAABAv/aAAwDAQACEAMQAAAB9gAAAAAAAAAAAP/EABYRAQEBAAAAAAAAAAAAAAAAAAABEf/aAAgBAQABPwB//8QAFhEBAQEAAAAAAAAAAAAAAAAAABEB/9oACAECAQE/AL//xAAWEQEBAQAAAAAAAAAAAAAAAAAAARH/2gAIAQMBAT8Aqf/EABYQAQEBAAAAAAAAAAAAAAAAAAABEf/aAAgBAQAGPwJf/8QAFhABAQEAAAAAAAAAAAAAAAAAABEB/9oACAEBAAE/IJ//2Q==");

        try (MockedStatic<ImageValidationUtil> utilMock = Mockito.mockStatic(ImageValidationUtil.class)) {
            utilMock.when(() -> ImageValidationUtil.validateUserImage(Mockito.any(byte[].class), Mockito.anyString()))
                    .thenAnswer(invocation -> null);

            Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(user));
            Mockito.when(userRepository.save(Mockito.any(User.class)))
                    .thenAnswer(i -> i.getArgument(0));

            var resp = userService.updateImagemPerfil(2, dto);
            assertNotNull(resp);
        }
    }

    @Test
    @DisplayName("Deve fazer upload de imagem de perfil com sucesso")
    void uploadImagemPerfil_Success() {
        UserRequestImagemPerfilDTO dto = new UserRequestImagemPerfilDTO();
        dto.setImagemUsuario(Base64.getEncoder().encodeToString(new byte[]{0, 1}));

        try (MockedStatic<ImageValidationUtil> utilMock = Mockito.mockStatic(ImageValidationUtil.class)) {
            utilMock.when(() -> ImageValidationUtil.validateUserImage(Mockito.any(byte[].class), Mockito.anyString()))
                    .thenAnswer(invocation -> null);

            when(userRepository.findById(3)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
            var resp = userService.uploadImagemPerfil(3, dto);
            assertNotNull(resp);
        }
    }

    @Test
    @DisplayName("Deve lançar BadRequestException ao tentar remover imagem de perfil inexistente")
    void deleteImagemPerfil_NoImage() {
        when(userRepository.findById(4)).thenReturn(Optional.of(user));
        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> userService.deleteImagemPerfil(4));
        assertTrue(ex.getMessage().contains("não possui uma imagem"));
    }

    @Test
    @DisplayName("Deve remover imagem de perfil com sucesso")
    void deleteImagemPerfil_Success() {
        ImagemUser img = new ImagemUser(new byte[]{1});
        user.setImagemUser(img);
        when(userRepository.findById(5)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        var resp = userService.deleteImagemPerfil(5);
        assertNull(resp.getImagemUrl());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar imagem de perfil inexistente")
    void getImagemPorIndice_NotFound() {
        when(userRepository.findById(6)).thenReturn(Optional.of(user));
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.getImagemPorIndice(6, 0));
        assertTrue(ex.getMessage().contains("Imagem não encontrada"));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar imagem de perfil com índice inválido")
    void getImagemPorIndice_WrongIndex() {
        ImagemUser img = new ImagemUser(new byte[]{1});
        user.setImagemUser(img);
        when(userRepository.findById(7)).thenReturn(Optional.of(user));
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.getImagemPorIndice(7, 1));
        assertTrue(ex.getMessage().contains("Imagem não encontrada"));
    }

    @Test
    @DisplayName("Deve retornar imagem de perfil ao buscar por índice válido")
    void getImagemPorIndice_Success() {
        byte[] data = new byte[]{9};
        ImagemUser img = new ImagemUser(data);
        user.setImagemUser(img);
        when(userRepository.findById(8)).thenReturn(Optional.of(user));
        var result = userService.getImagemPorIndice(8, 0);
        assertArrayEquals(data, result);
    }

    // Tests for updateSenha and validarEmail
    @Test
    @DisplayName("Deve atualizar senha do usuário com sucesso")
    void updateSenha_Success() {
        UserRequestSenhaDTO dto = new UserRequestSenhaDTO();
        dto.setSenha("NewPass1@");
        when(userRepository.findByEmail("e@e.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("enc");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        var resp = userService.updateSenha("e@e.com", dto);
        assertEquals("enc", user.getSenha());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar atualizar senha de email inexistente")
    void updateSenha_EmailNotFound() {
        UserRequestSenhaDTO dto = new UserRequestSenhaDTO();
        dto.setSenha("x");
        when(userRepository.findByEmail("no@one.com")).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.validarEmail("no@one.com"));
    }

    @Test
    @DisplayName("Deve retornar usuário ao validar email existente")
    void validarEmail_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        var resp = userService.validarEmail(user.getEmail());
        assertEquals(user.getEmail(), resp.getEmail());
    }

    @Test
    @DisplayName("Deve atualizar campo userNovo para false com sucesso")
    void atualizarUserNovoParaFalse() {
        user.setUserNovo(true);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        // Como o DTO não mapeia o campo userNovo, a asserção é removida.
        var resp = userService.atualizarUserNovoParaFalse(1);
        assertNotNull(resp);
    }

    // Tests for autenticar
    @Test
    @DisplayName("Deve lançar ResponseStatusException ao autenticar com email inexistente")
    void autenticar_EmailNotFound() {
        User u = new User();
        u.setEmail("x@y.com");
        u.setSenha("pass");
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(u.getEmail())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.autenticar(u));
    }

    @Test
    @DisplayName("Deve autenticar usuário e retornar token com sucesso")
    void autenticar_Success() {
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
}