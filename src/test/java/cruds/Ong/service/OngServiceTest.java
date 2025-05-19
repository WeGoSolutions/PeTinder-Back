package cruds.Ong.service;

import cruds.Imagem.entity.ImagemOng;
import cruds.Ong.controller.dto.request.OngRequestCriarDTO;
import cruds.Ong.controller.dto.request.OngRequestUpdateDTO;
import cruds.Ong.controller.dto.response.OngResponseDTO;
import cruds.Ong.controller.dto.response.OngResponseLoginDTO;
import cruds.Ong.entity.Ong;
import cruds.Ong.repository.OngRepository;
import cruds.common.exception.ConflictException;
import cruds.common.strategy.ImageStorageStrategy;
import cruds.common.util.ImageValidationUtil;
import cruds.config.token.GerenciadorTokenJwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OngServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Mock
    private OngRepository ongRepository;
    @Mock
    private ImageStorageStrategy imageStorageStrategy;

    @InjectMocks
    private OngService ongService;

    private OngRequestCriarDTO criarDto;
    private OngRequestUpdateDTO updateDto;
    private String imageName;
    private byte[] imageBytes;

    @BeforeEach
    void setup() {
        criarDto = new OngRequestCriarDTO();
        criarDto.setEmail("test@ong.com");
        criarDto.setSenha("pass123");
        criarDto.setNome("ONG Test");
        criarDto.setCnpj("00.000.000/0001-00");

        updateDto = new OngRequestUpdateDTO();
        updateDto.setEmail("upd@ong.com");
        updateDto.setSenha("newpass");
        updateDto.setNome("ONG Upd");
        updateDto.setCnpj("11.111.111/1111-11");
        updateDto.setCpf("12345678901");
        updateDto.setRazaoSocial("Razao");
        updateDto.setLink("http://link");

        imageName = "ong.jpg";
        imageBytes = "data".getBytes();
    }

    @Test
    void criarOng_success() {
        when(ongRepository.findByEmail(criarDto.getEmail())).thenReturn(Optional.empty());
        Ong ong = new Ong();
        ong.setId(1);
        ong.setEmail(criarDto.getEmail());
        when(ongRepository.save(any())).thenReturn(ong);

        Ong result = ongService.criarOng(criarDto);
        assertEquals(1, result.getId());
    }

    @Test
    void criarOng_conflictEmail() {
        when(ongRepository.findByEmail(criarDto.getEmail()))
                .thenReturn(Optional.of(new Ong()));
        ConflictException ex = assertThrows(ConflictException.class,
                () -> ongService.criarOng(criarDto));
        assertTrue(ex.getMessage().contains("Email ja cadastrado"));
    }

    @Test
    void login_success() {
        String email = "e@e.com";
        String senha = "s3nh@";
        Ong ong = new Ong();
        ong.setId(2);
        ong.setEmail(email);
        ong.setNome("Nome");
        when(ongRepository.findByEmailandSenha(email, senha))
                .thenReturn(Optional.of(ong));
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(gerenciadorTokenJwt.generateToken(auth)).thenReturn("tok");

        OngResponseLoginDTO resp = ongService.login(email, senha);
        assertEquals(2, resp.getId());
        assertEquals(email, resp.getEmail());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void getOng_success() {
        Ong ong = new Ong();
        ong.setId(3);
        when(ongRepository.findById(3)).thenReturn(Optional.of(ong));
        OngResponseDTO resp = ongService.getOng(3);
        assertEquals(3, resp.getId());
    }

    @Test
    void getOng_notFound_conflict() {
        when(ongRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(ConflictException.class, () -> ongService.getOng(4));
    }

    @Test
    void updateOng_success() {
        Ong existing = new Ong();
        existing.setId(5);
        existing.setEmail("old@e.com");
        when(ongRepository.findById(5)).thenReturn(Optional.of(existing));
        when(ongRepository.findByEmail(updateDto.getEmail())).thenReturn(Optional.empty());
        when(ongRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        OngResponseDTO resp = ongService.updateOng(5, updateDto);
        assertEquals(5, resp.getId());
    }

    @Test
    void updateOng_conflictEmail() {
        Ong existing = new Ong();
        existing.setId(6);
        existing.setEmail("a@a.com");
        when(ongRepository.findById(6)).thenReturn(Optional.of(existing));
        when(ongRepository.findByEmail(updateDto.getEmail()))
                .thenReturn(Optional.of(new Ong()));
        assertThrows(ConflictException.class,
                () -> ongService.updateOng(6, updateDto));
    }

    @Test
    void updateOng_notFound_conflict() {
        when(ongRepository.findById(7)).thenReturn(Optional.empty());
        assertThrows(ConflictException.class,
                () -> ongService.updateOng(7, updateDto));
    }

    @Test
    void updateImageOng_success() throws IOException {
        Ong ong = new Ong();
        ong.setId(8);
        when(ongRepository.findById(8)).thenReturn(Optional.of(ong));
        try (MockedStatic<ImageValidationUtil> util = mockStatic(ImageValidationUtil.class)) {
            util.when(() -> ImageValidationUtil.validateOngImage(imageBytes, imageName))
                    .thenAnswer(invocation -> null);
            doNothing().when(imageStorageStrategy).salvarImagem(eq(imageBytes), anyString());
            when(ongRepository.save(any())).thenReturn(ong);

            Ong result = ongService.updateImageOng(8, imageName, imageBytes);
            assertEquals(8, result.getId());
        }
    }

    @Test
    void updateImageOng_storageError_runtime() throws IOException {
        Ong ong = new Ong();
        ong.setId(10);
        when(ongRepository.findById(10)).thenReturn(Optional.of(ong));
        try (MockedStatic<ImageValidationUtil> util = mockStatic(ImageValidationUtil.class)) {
            util.when(() -> ImageValidationUtil.validateOngImage(imageBytes, imageName))
                    .thenAnswer(invocation -> null);
            doThrow(new IOException("disk"))
                    .when(imageStorageStrategy).salvarImagem(eq(imageBytes), anyString());
            assertThrows(RuntimeException.class,
                    () -> ongService.updateImageOng(10, imageName, imageBytes));
        }
    }

    @Test
    void getImageOng_success() {
        Ong ong = new Ong();
        ong.setId(11);
        ImagemOng img = new ImagemOng();
        ong.setImagemOng(img);
        when(ongRepository.findById(11)).thenReturn(Optional.of(ong));
        OngResponseDTO resp = ongService.getImageOng(11);
        assertEquals(11, resp.getId());
    }

    @Test
    void getImageOng_noImage_conflict() {
        Ong ong = new Ong();
        ong.setId(12);
        when(ongRepository.findById(12)).thenReturn(Optional.of(ong));
        assertThrows(ConflictException.class,
                () -> ongService.getImageOng(12));
    }
}