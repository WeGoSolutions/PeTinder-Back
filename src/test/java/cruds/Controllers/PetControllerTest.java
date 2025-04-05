// File: `PetControllerTest.java`
package cruds.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import cruds.Pets.controller.PetController;
import cruds.Pets.Repositorys.PetRepository;
import cruds.Pets.entity.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(PetControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetController petController;

    @Autowired
    private ObjectMapper objectMapper;

    private PetDTO validPetDTO;
    private Pet petEntity;

    @BeforeEach
    void setup() {
        validPetDTO = new PetDTO();
        validPetDTO.setNome("Buddy");
        validPetDTO.setIdade(3.0);
        validPetDTO.setPeso(12.5);
        validPetDTO.setAltura(0.5);
        validPetDTO.setCurtidas(10);
        validPetDTO.setTags(List.of("friendly", "small"));
        // Create a sample base64 image string
        String imgBase64 = Base64.getEncoder().encodeToString("fakeImageData".getBytes());
        validPetDTO.setImagemBase64(List.of(imgBase64));

        petEntity = new Pet();
        petEntity.setId(1);
        petEntity.setNome(validPetDTO.getNome());
        petEntity.setIdade(validPetDTO.getIdade());
        petEntity.setPeso(validPetDTO.getPeso());
        petEntity.setAltura(validPetDTO.getAltura());
        petEntity.setCurtidas(validPetDTO.getCurtidas());
        petEntity.setTags(validPetDTO.getTags());
        // convert base64 to bytes
        List<byte[]> imagensBytes = new ArrayList<>();
        imagensBytes.add(Base64.getDecoder().decode(imgBase64));
        petEntity.setImagem(imagensBytes);
    }

    @Test
    void testCadastrarPetValid() throws Exception {
        logger.info("Testando cadastro de pet válido");
        when(petRepository.save(any(Pet.class))).thenReturn(petEntity);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPetDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(petEntity.getId()))
                .andExpect(jsonPath("$.nome").value(petEntity.getNome()));
    }

    @Test
    void testCadastrarPetInvalid_MissingNome() throws Exception {
        logger.info("Testando cadastro de pet com nome inválido/missing");
        validPetDTO.setNome(" ");
        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPetDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testListarGeral_NoData() throws Exception {
        logger.info("Testando listagem geral sem dados");
        when(petRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/pets"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListarGeral_WithData() throws Exception {
        logger.info("Testando listagem geral com dados");
        when(petRepository.findAll()).thenReturn(List.of(petEntity));

        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(petEntity.getId()));
    }

    @Test
    void testAtualizarPetFound() throws Exception {
        logger.info("Testando atualização de pet existente");
        when(petRepository.existsById(1)).thenReturn(true);
        when(petRepository.findById(1)).thenReturn(Optional.of(petEntity));
        when(petRepository.save(any(Pet.class))).thenReturn(petEntity);

        validPetDTO.setNome("Max");
        mockMvc.perform(put("/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPetDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.nome").value("Max"));
    }

    @Test
    void testAtualizarPetNotFound() throws Exception {
        logger.info("Testando atualização de pet inexistente");
        when(petRepository.existsById(1)).thenReturn(false);

        mockMvc.perform(put("/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPetDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePetFound() throws Exception {
        logger.info("Testando deleção de pet existente");
        when(petRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/pets/1"))
                .andExpect(status().isResetContent());
    }

    @Test
    void testDeletePetNotFound() throws Exception {
        logger.info("Testando deleção de pet inexistente");
        when(petRepository.existsById(1)).thenReturn(false);

        mockMvc.perform(delete("/pets/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListarUrlsImagens_PetNotFound() throws Exception {
        logger.info("Testando listagem de URLs de imagens para pet inexistente");
        when(petRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/pets/1/imagens"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListarUrlsImagens_NoImages() throws Exception {
        logger.info("Testando listagem de URLs de imagens para pet sem imagens");
        petEntity.setImagem(Collections.emptyList());
        when(petRepository.findById(1)).thenReturn(Optional.of(petEntity));

        mockMvc.perform(get("/pets/1/imagens"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetImagemPorIndice_Valid() throws Exception {
        logger.info("Testando recuperação de imagem por índice válido");
        when(petRepository.findById(1)).thenReturn(Optional.of(petEntity));

        mockMvc.perform(get("/pets/1/imagens/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));
    }

    @Test
    void testGetImagemPorIndice_InvalidIndex() throws Exception {
        logger.info("Testando recuperação de imagem por índice inválido");
        when(petRepository.findById(1)).thenReturn(Optional.of(petEntity));

        mockMvc.perform(get("/pets/1/imagens/10"))
                .andExpect(status().isNotFound());
    }
}