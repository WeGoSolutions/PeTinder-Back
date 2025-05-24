package cruds.Pets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cruds.Pets.controller.dto.request.PetRequestCriarDTO;
import cruds.Pets.controller.dto.request.PetRequestCurtirDTO;
import cruds.Pets.controller.dto.request.UploadImagesRequest;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.service.PetService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@WithMockUser
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetService petService;

    @Test
    @DisplayName("Testa criação de pet com sucesso via endpoint POST /pets")
    void testCadastrarPet() throws Exception {
        PetRequestCriarDTO req = new PetRequestCriarDTO();
        req.setNome("Rex");
        req.setIdade(3.0);
        req.setPeso(10.0);
        req.setAltura(40.0);
        req.setCurtidas(0);
        req.setTags(List.of("tag1"));
        req.setDescricao("Descrição");
        req.setImagemBase64(List.of("imagemBase64"));

        Pet pet = new Pet();
        pet.setId(1);
        pet.setNome("Rex");
        when(petService.cadastrarPet(any())).thenReturn(pet);

        mockMvc.perform(post("/pets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Rex"));
    }

    @Test
    @DisplayName("Testa upload de imagens para pet com sucesso via endpoint POST /pets/{id}/upload-imagens")
    void testUploadPetImages() throws Exception {
        byte[] img = new byte[]{1,2,3};
        String nome = "img.jpg";
        UploadImagesRequest req = new UploadImagesRequest();
        req.setImagensBytes(List.of(img));
        req.setNomesArquivos(List.of(nome));

        Pet pet = new Pet();
        pet.setId(2);
        when(petService.uploadPetImages(eq(2), anyList(), anyList()))
                .thenReturn(pet);

        mockMvc.perform(post("/pets/2/upload-imagens")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    @DisplayName("Testa listagem de URLs de imagens para o pet via endpoint GET /pets/{id}/imagens")
    void testListarUrlsImagens() throws Exception {
        List<String> urls = List.of("/img1.jpg", "/img2.jpg");
        when(petService.listarUrlsImagens(any(HttpServletRequest.class), eq(3)))
                .thenReturn(urls);

        mockMvc.perform(get("/pets/3/imagens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0]").value("/img1.jpg"));
    }

    @Test
    @DisplayName("Testa listagem geral de pets via endpoint GET /pets")
    void testListarGeral() throws Exception {
        PetResponseGeralDTO dto = new PetResponseGeralDTO();
        dto.setId(4);
        when(petService.listarGeral()).thenReturn(List.of(dto));

        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(4));
    }

    @Test
    @DisplayName("Testa obtenção de uma imagem específica do pet via endpoint GET /pets/{id}/imagens/{indice}")
    void testGetImagemPorIndice() throws Exception {
        byte[] data = new byte[]{5};
        when(petService.getImagemPorIndice(5, 0)).thenReturn(data);

        mockMvc.perform(get("/pets/5/imagens/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(data));
    }

    @Test
    @DisplayName("Testa atualização de dados do pet com sucesso via endpoint PUT /pets/{id}")
    void testAtualizar() throws Exception {
        PetRequestCriarDTO req = new PetRequestCriarDTO();
        req.setNome("Max");
        req.setIdade(2.0);
        req.setPeso(8.0);
        req.setAltura(35.0);
        req.setCurtidas(0);
        req.setTags(List.of("tag2"));
        req.setDescricao("Nova descrição");
        req.setImagemBase64(List.of("imagemBase64Atualizada"));

        Pet pet = new Pet();
        pet.setId(6);
        pet.setNome("Max");
        when(petService.atualizar(eq(6), any())).thenReturn(pet);

        mockMvc.perform(put("/pets/6")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.nome").value("Max"));
    }

    @Test
    @DisplayName("Testa remoção de pet com sucesso via endpoint DELETE /pets/{id}")
    void testDeletar() throws Exception {
        doNothing().when(petService).deletarPet(7);

        mockMvc.perform(delete("/pets/7")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Testa remoção de imagem do pet com sucesso via endpoint DELETE /pets/{id}/imagens/{indice}")
    void testApagarImagem() throws Exception {
        doNothing().when(petService).apagarImagem(9, 1);

        mockMvc.perform(delete("/pets/9/imagens/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}