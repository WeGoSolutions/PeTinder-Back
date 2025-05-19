package cruds.Pets.service;

import cruds.Imagem.entity.Imagem;
import cruds.Imagem.repository.ImagemRepository;
import cruds.Pets.controller.dto.request.PetRequestCriarDTO;
import cruds.Pets.controller.dto.request.PetRequestCurtirDTO;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.repository.PetRepository;
import cruds.common.exception.*;
import cruds.common.strategy.ImageStorageStrategy;
import cruds.common.util.ImageValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private ImagemRepository imagemRepository;
    @Mock
    private ImageStorageStrategy imageStorageStrategy;
    @InjectMocks
    private PetService petService;

    private PetRequestCriarDTO criarDto;
    private String validBase64;
    private byte[] decoded;

    @BeforeEach
    void setUp() {
        decoded = "data".getBytes();
        validBase64 = Base64.getEncoder().encodeToString(decoded);
        criarDto = new PetRequestCriarDTO();
        criarDto.setNome("Pet");
        criarDto.setImagemBase64(List.of(validBase64));
    }

    @Test
    @DisplayName("Deve cadastrar pet com sucesso")
    void cadastrarPet_success() throws IOException {
        Pet saved = new Pet(); saved.setId(1);
        when(petRepository.save(any())).thenReturn(saved);
        try (MockedStatic<ImageValidationUtil> util = mockStatic(ImageValidationUtil.class)) {
            util.when(() -> ImageValidationUtil.validatePetImages(any(), any()))
                    .thenAnswer(invocation -> null);
            when(imageStorageStrategy.gerarCaminho(anyString())).thenReturn("path.jpg");
            doNothing().when(imageStorageStrategy).salvarImagem(any(), anyString());

            Pet result = petService.cadastrarPet(criarDto);
            assertEquals(1, result.getId());
            verify(imagemRepository).saveAll(any());
        }
    }

    @Test
    @DisplayName("Deve lançar ConflictException ao cadastrar pet com Base64 inválido")
    void cadastrarPet_invalidBase64_conflict() {
        criarDto.setImagemBase64(List.of("!!!"));
        assertThrows(ConflictException.class, () -> petService.cadastrarPet(criarDto));
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando ocorrer erro na validação das imagens")
    void cadastrarPet_validationError_badRequest() throws IOException {
        Pet saved = new Pet(); saved.setId(2);
        when(petRepository.save(any())).thenReturn(saved);
        try (MockedStatic<ImageValidationUtil> util = mockStatic(ImageValidationUtil.class)) {
            util.when(() -> ImageValidationUtil.validatePetImages(any(), any()))
                    .thenThrow(new IOException("fail"));
            assertThrows(BadRequestException.class, () -> petService.cadastrarPet(criarDto));
        }
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando ocorrer erro ao salvar imagens no armazenamento")
    void cadastrarPet_storageError_runtime() throws IOException {
        Pet saved = new Pet(); saved.setId(3);
        when(petRepository.save(any())).thenReturn(saved);
        try (MockedStatic<ImageValidationUtil> util = mockStatic(ImageValidationUtil.class)) {
            util.when(() -> ImageValidationUtil.validatePetImages(any(), any()))
                    .thenAnswer(invocation -> null);
            when(imageStorageStrategy.gerarCaminho(anyString())).thenReturn("p.jpg");
            doThrow(new IOException("disk"))
                    .when(imageStorageStrategy).salvarImagem(any(), anyString());
            assertThrows(RuntimeException.class, () -> petService.cadastrarPet(criarDto));
        }
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar atualizar pet inexistente")
    void atualizar_notFound() {
        when(petRepository.existsById(5)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> petService.atualizar(5, criarDto));
    }

    @Test
    @DisplayName("Deve lançar BadRequestException ao atualizar pet com Base64 inválido")
    void atualizar_invalidBase64_badRequest() {
        when(petRepository.existsById(6)).thenReturn(true);
        Pet exists = new Pet(); exists.setId(6);
        when(petRepository.findById(6)).thenReturn(Optional.of(exists));
        criarDto.setImagemBase64(List.of("!!!"));
        assertThrows(BadRequestException.class, () -> petService.atualizar(6, criarDto));
    }

    @Test
    @DisplayName("Deve lançar BadRequestException ao ocorrer erro na validação das imagens na atualização")
    void atualizar_validationError_badRequest() throws IOException {
        when(petRepository.existsById(7)).thenReturn(true);
        Pet exists = new Pet(); exists.setId(7);
        when(petRepository.findById(7)).thenReturn(Optional.of(exists));
        try (MockedStatic<ImageValidationUtil> util = mockStatic(ImageValidationUtil.class)) {
            util.when(() -> ImageValidationUtil.validatePetImages(any(), any()))
                    .thenThrow(new IOException("err"));
            assertThrows(BadRequestException.class, () -> petService.atualizar(7, criarDto));
        }
    }

    @Test
    @DisplayName("Deve lançar RuntimeException ao ocorrer erro de armazenamento durante a atualização")
    void atualizar_storageError_runtime() throws IOException {
        when(petRepository.existsById(8)).thenReturn(true);
        Pet exists = new Pet(); exists.setId(8);
        when(petRepository.findById(8)).thenReturn(Optional.of(exists));
        try (MockedStatic<ImageValidationUtil> util = mockStatic(ImageValidationUtil.class)) {
            util.when(() -> ImageValidationUtil.validatePetImages(any(), any()))
                    .thenAnswer(invocation -> null);
            doThrow(new IOException("disk"))
                    .when(imageStorageStrategy).salvarImagem(any(), anyString());
            assertThrows(RuntimeException.class, () -> petService.atualizar(8, criarDto));
        }
    }

    @Test
    @DisplayName("Deve lançar NoContentException quando nenhum pet for encontrado na listagem geral")
    void listarGeral_noContent() {
        when(petRepository.findAll()).thenReturn(List.of());
        assertThrows(NoContentException.class, () -> petService.listarGeral());
    }

    @Test
    @DisplayName("Deve retornar lista de pets na listagem geral com sucesso")
    void listarGeral_success() {
        Pet p = new Pet(); p.setId(10);
        when(petRepository.findAll()).thenReturn(List.of(p));
        List<PetResponseGeralDTO> list = petService.listarGeral();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Deve lançar NoContentException ao buscar pet inexistente por id")
    void obterPetPorId_notFound() {
        when(petRepository.findById(11)).thenReturn(Optional.empty());
        assertThrows(NoContentException.class, () -> petService.obterPetPorId(11));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao listar URLs quando não há imagens para o pet")
    void listarUrlsImagens_noImages_notFound() {
        Pet p = new Pet(); p.setId(12);
        p.setImagens(List.of());
        when(petRepository.findById(12)).thenReturn(Optional.of(p));
        assertThrows(NotFoundException.class, () -> petService.listarUrlsImagens(mock(HttpServletRequest.class), 12));
    }

    @Test
    @DisplayName("Deve retornar URLs das imagens do pet com sucesso")
    void listarUrlsImagens_success() {
        Pet p = new Pet(); p.setId(13);
        Imagem img = new Imagem("path", p);
        p.setImagens(List.of(img));
        when(petRepository.findById(13)).thenReturn(Optional.of(p));
        HttpServletRequest req = mock(HttpServletRequest.class);
        List<String> urls = petService.listarUrlsImagens(req, 13);
        assertFalse(urls.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar deletar pet inexistente")
    void deletarPet_notFound() {
        when(petRepository.existsById(14)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> petService.deletarPet(14));
    }

    @Test
    @DisplayName("Deve deletar pet com sucesso")
    void deletarPet_success() {
        when(petRepository.existsById(15)).thenReturn(true);
        doNothing().when(petRepository).deleteById(15);
        assertDoesNotThrow(() -> petService.deletarPet(15));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar imagem por índice se não há imagens")
    void getImagemPorIndice_noImages_notFound() {
        Pet p = new Pet(); p.setId(16);
        p.setImagens(List.of());
        when(petRepository.findById(16)).thenReturn(Optional.of(p));
        assertThrows(NotFoundException.class, () -> petService.getImagemPorIndice(16, 0));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar imagem por índice inválido")
    void getImagemPorIndice_invalidIndex_notFound() {
        Pet p = new Pet(); p.setId(17);
        Imagem img = new Imagem("/tmp/noexist", p);
        p.setImagens(List.of(img));
        when(petRepository.findById(17)).thenReturn(Optional.of(p));
        assertThrows(NotFoundException.class, () -> petService.getImagemPorIndice(17, 5));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando ocorrer erro de I\\/O ao ler imagem")
    void getImagemPorIndice_ioError_runtime() throws IOException {
        Pet p = new Pet(); p.setId(18);
        Imagem img = new Imagem("/tmp/noexist", p);
        p.setImagens(List.of(img));
        when(petRepository.findById(18)).thenReturn(Optional.of(p));
        try (MockedStatic<Files> files = mockStatic(Files.class)) {
            files.when(() -> Files.readAllBytes(Paths.get(img.getCaminho())))
                    .thenThrow(new IOException("fail"));
            assertThrows(RuntimeException.class, () -> petService.getImagemPorIndice(18, 0));
        }
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar curtir pet inexistente")
    void curtirPet_notFound() {
        when(petRepository.findById(19)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> petService.curtirPet(19, new PetRequestCurtirDTO()));
    }

    @Test
    @DisplayName("Deve incrementar e decrementar curtidas ao curtir e descurtir pet")
    void curtirPet_likeAndUnlike() {
        Pet p = new Pet();
        p.setId(20);
        p.setCurtidas(5);
        when(petRepository.findById(20)).thenReturn(Optional.of(p));
        when(petRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PetRequestCurtirDTO dto = new PetRequestCurtirDTO();
        dto.setIsLiked(true);
        Pet liked = petService.curtirPet(20, dto);
        assertEquals(6, liked.getCurtidas());

        dto.setIsLiked(false);
        Pet unliked = petService.curtirPet(20, dto);
        assertEquals(5, unliked.getCurtidas());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar apagar imagem se não houver imagens")
    void apagarImagem_noImages_notFound() {
        when(petRepository.findById(21)).thenReturn(Optional.of(new Pet()));
        assertThrows(NotFoundException.class, () -> petService.apagarImagem(21, 0));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar apagar imagem com índice inválido")
    void apagarImagem_invalidIndex_notFound() {
        Pet p = new Pet(); p.setImagens(List.of(new Imagem("p", p)));
        when(petRepository.findById(22)).thenReturn(Optional.of(p));
        assertThrows(NotFoundException.class, () -> petService.apagarImagem(22, 5));
    }

    @Test
    @DisplayName("Deve apagar imagem com sucesso")
    void apagarImagem_success() {
        Pet p = new Pet(); p.setImagens(new java.util.ArrayList<>(List.of(new Imagem("p", p))));
        when(petRepository.findById(23)).thenReturn(Optional.of(p));
        petService.apagarImagem(23, 0);
        verify(petRepository).save(p);
    }
}