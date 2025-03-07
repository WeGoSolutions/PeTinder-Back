// java
package sptech.school.crud_imagem.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sptech.school.crud_imagem.DTOs.PetDTO;
import sptech.school.crud_imagem.Tables.Pet;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Order(1)
    public void testCreatePet() throws Exception {
        PetDTO dto = new PetDTO();
        dto.setNome("Pet Teste");
        dto.setIdade(2.0);
        dto.setPeso(5.0);
        dto.setAltura(0.5);
        dto.setCurtidas(0);
        dto.setTags(Arrays.asList("amigavel", "ativo"));
        // Nenhuma imagem para este teste

        mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @Order(2)
    public void testListAllPets() throws Exception {
        mockMvc.perform(get("/pet"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void testUpdatePet() throws Exception {
        // Cria pet
        PetDTO dto = new PetDTO();
        dto.setNome("Pet para Atualizar");
        dto.setIdade(3.0);
        dto.setPeso(6.0);
        dto.setAltura(0.6);
        dto.setCurtidas(5);
        dto.setTags(Arrays.asList("bravo"));

        String content = mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Pet pet = mapper.readValue(content, Pet.class);

        // Atualiza
        dto.setNome("Pet Atualizado");
        mockMvc.perform(put("/pet/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.nome").value("Pet Atualizado"));
    }

    @Test
    @Order(4)
    public void testListPetImagesAndGetImageByIndex() throws Exception {
        // Cria pet com imagem (imagem codificada em Base64 para teste)
        PetDTO dto = new PetDTO();
        dto.setNome("Pet Com Imagem");
        dto.setIdade(4.0);
        dto.setPeso(7.0);
        dto.setAltura(0.7);
        dto.setCurtidas(10);
        dto.setTags(Arrays.asList("fofo"));
        dto.setImagemBase64(Arrays.asList("dGVzdGltYWdl")); // Base64 simulada

        String content = mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Pet pet = mapper.readValue(content, Pet.class);

        // Consulta URLs das imagens
        mockMvc.perform(get("/pet/" + pet.getId() + "/imagens"))
                .andExpect(status().isOk());

        // Consulta imagem por índice
        mockMvc.perform(get("/pet/" + pet.getId() + "/imagens/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE));
    }

    @Test
    @Order(5)
    public void testDeletePet() throws Exception {
        // Cria pet para deleção
        PetDTO dto = new PetDTO();
        dto.setNome("Pet a Excluir");
        dto.setIdade(1.0);
        dto.setPeso(3.0);
        dto.setAltura(0.3);
        dto.setCurtidas(0);
        dto.setTags(Arrays.asList("quieto"));

        String content = mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Pet pet = mapper.readValue(content, Pet.class);

        // Deleta o pet
        mockMvc.perform(delete("/pet/" + pet.getId()))
                .andExpect(status().is(205));
    }
}