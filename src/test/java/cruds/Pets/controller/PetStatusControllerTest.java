package cruds.Pets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.controller.dto.response.PetResponsePendingOngDTO;
import cruds.Pets.controller.dto.response.PetResponseUserPendenteDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.repository.PetRepository;
import cruds.Pets.repository.PetStatusRepository;
import cruds.Pets.service.PetService;
import cruds.Pets.service.PetStatusService;
import cruds.Users.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import cruds.Pets.controller.dto.request.PetStatusRequestDTO;
import cruds.Pets.controller.dto.response.PetStatusResponseDTO;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PetStatusController.class)
@WithMockUser
class PetStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetStatusService petStatusService;

    @MockitoBean
    private PetStatusRepository petStatusRepository;

    @MockitoBean
    private PetService petService;

    @MockitoBean
    private PetRepository petRepository;

    @Test
    @DisplayName("Deve listar pets curtidos (listarCurtidos) com sucesso")
    void testListarCurtidos_success() throws Exception {
        PetStatus status = new PetStatus();
        status.setStatus(PetStatusEnum.LIKED);

        Pet pet = new Pet();
        pet.setId(1);
        status.setPet(pet);

        User user = new User();
        user.setId(1L);
        status.setUser(user);

        when(petStatusRepository.findAllLikedStatusPets()).thenReturn(List.of(status));

        mockMvc.perform(get("/status/liked"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("Deve retornar 204 se não houver pets curtidos (listarCurtidos)")
    void testListarCurtidos_empty() throws Exception {
        when(petStatusRepository.findAllLikedStatusPets()).thenReturn(List.of());

        mockMvc.perform(get("/status/liked"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve listar todos os pets com status (listarTodos) com sucesso")
    void testListarTodos_success() throws Exception {
        PetStatus status = new PetStatus();
        status.setStatus(PetStatusEnum.LIKED);

        Pet pet = new Pet();
        pet.setId(1);
        status.setPet(pet);

        User user = new User();
        user.setId(1L);
        status.setUser(user);

        PetStatusResponseDTO dto = new PetStatusResponseDTO(status);

        when(petStatusService.getAllPetsWithUserStatus()).thenReturn(List.of(dto));

        mockMvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("Deve retornar 204 se não houver pets (listarTodos)")
    void testListarTodos_empty() throws Exception {
        when(petStatusService.getAllPetsWithUserStatus()).thenReturn(List.of());

        mockMvc.perform(get("/status"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve criar ou atualizar status do pet (createOrUpdatePetStatus) com sucesso")
    void testCreateOrUpdatePetStatus_success() throws Exception {
        PetStatusRequestDTO dto = new PetStatusRequestDTO();
        dto.setPetId(1);
        dto.setUserId(2);
        dto.setStatus(PetStatusEnum.LIKED);

        PetStatus status = new PetStatus();
        status.setStatus(PetStatusEnum.LIKED);

        Pet pet = new Pet();
        pet.setId(1);
        status.setPet(pet);

        User user = new User();
        user.setId(1L);
        status.setUser(user);

        when(petStatusService.createOrUpdatePetStatus(any())).thenReturn(status);

        mockMvc.perform(post("/status")
                        .with(csrf()) // Adicione esta linha
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("LIKED"));
    }

    @Test
    @DisplayName("Deve listar pets disponíveis para o usuário via GET /status/disponivel/{userId}")
    void testListAvailablePetsForUser() throws Exception {
        int userId = 42;
        PetResponseGeralDTO dto = new PetResponseGeralDTO();
        dto.setId(100);
        when(petStatusService.listAvailablePetsForUser(userId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/status/disponivel/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(100));
    }

    @Test
    @DisplayName("Deve listar pets por usuário e status via GET /status/{userId}/{status}")
    void testGetPetsByUserAndStatus() throws Exception {
        int userId = 7;
        String status = "LIKED";

        PetStatus petStatus = new PetStatus();
        petStatus.setStatus(PetStatusEnum.LIKED);

        Pet pet = new Pet();
        pet.setId(55);
        petStatus.setPet(pet);

        User user = new User();
        user.setId((long) userId);
        petStatus.setUser(user);

        when(petStatusService.getPetsByUserAndStatus(userId, PetStatusEnum.LIKED)).thenReturn(List.of(petStatus));

        mockMvc.perform(get("/status/{userId}/{status}", userId, status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].status").value("LIKED"));
    }

    @Test
    @DisplayName("Deve remover o status do pet para o usuário via DELETE /status/{petId}/{userId}")
    void testDeletePetStatus() throws Exception {
        int petId = 10;
        int userId = 20;

        Mockito.doNothing().when(petStatusService).deletePetStatus(petId, userId);

        mockMvc.perform(delete("/status/{petId}/{userId}", petId, userId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve listar pets com status padrão para ONGs via GET /status/default/{userId}")
    void testListDefaultPets() throws Exception {
        int userId = 5;
        PetResponseGeralDTO dto = new PetResponseGeralDTO();
        dto.setId(123);
        when(petStatusService.listDefaultPets(userId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/status/default/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(123));
    }

    @Test
    @DisplayName("Deve definir status LIKED para um pet via POST /status/liked/{petId}/{userId}")
    void testSetLikedStatus() throws Exception {
        int petId = 10;
        int userId = 20;

        when(petStatusRepository.findByPetIdAndUserId(petId, userId)).thenReturn(Optional.empty());

        PetStatus status = new PetStatus();
        status.setStatus(PetStatusEnum.LIKED);
        Pet pet = new Pet();
        pet.setId(petId);
        status.setPet(pet);
        User user = new User();
        user.setId((long) userId);
        status.setUser(user);

        when(petStatusService.createOrUpdatePetStatus(any())).thenReturn(status);

        mockMvc.perform(post("/status/liked/{petId}/{userId}", petId, userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("LIKED"));
    }

    @Test
    @DisplayName("Deve adotar um pet via POST /status/adopted/{petId}/{userId}")
    void testAdoptPet() throws Exception {
        int petId = 30;
        int userId = 40;

        PetStatus status = new PetStatus();
        status.setStatus(PetStatusEnum.ADOPTED);
        Pet pet = new Pet();
        pet.setId(petId);
        status.setPet(pet);
        User user = new User();
        user.setId((long) userId);
        status.setUser(user);

        when(petStatusService.adoptPet(petId, userId)).thenReturn(status);

        mockMvc.perform(post("/status/adopted/{petId}/{userId}", petId, userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ADOPTED"));
    }

    @Test
    @DisplayName("Deve definir status PENDING para um pet via POST /status/pending/{petId}/{userId}")
    void testSetPendingStatus() throws Exception {
        int petId = 11;
        int userId = 22;

        PetStatus status = new PetStatus();
        status.setStatus(PetStatusEnum.PENDING);
        Pet pet = new Pet();
        pet.setId(petId);
        status.setPet(pet);
        User user = new User();
        user.setId((long) userId);
        status.setUser(user);

        when(petStatusService.createOrUpdatePetStatus(any())).thenReturn(status);

        mockMvc.perform(post("/status/pending/{petId}/{userId}", petId, userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Deve listar todos os status de um pet via GET /status/pet/{petId}")
    void testGetPetStatusList() throws Exception {
        int petId = 99;
        when(petStatusService.getPetStatusList(petId)).thenReturn(List.of(PetStatusEnum.LIKED, PetStatusEnum.PENDING));

        mockMvc.perform(get("/status/pet/{petId}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0]").value("LIKED"))
                .andExpect(jsonPath("$[1]").value("PENDING"));
    }

    @Test
    @DisplayName("Deve retornar 204 se não houver status para o pet em GET /status/pet/{petId}")
    void testGetPetStatusList_empty() throws Exception {
        int petId = 99;
        when(petStatusService.getPetStatusList(petId)).thenReturn(List.of());

        mockMvc.perform(get("/status/pet/{petId}", petId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve listar pets pendentes com informações das ONGs via GET /status/pending/ong/{userId}")
    void testListPendingPetsWithOngForUser() throws Exception {
        int userId = 77;
        PetResponsePendingOngDTO dto = new PetResponsePendingOngDTO();
        when(petStatusService.listPendingPetsWithOngForUser(eq(userId), any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/status/pending/ong/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("Deve retornar 204 se não houver pets pendentes com ONG via GET /status/pending/ong/{userId}")
    void testListPendingPetsWithOngForUser_empty() throws Exception {
        int userId = 77;
        when(petStatusService.listPendingPetsWithOngForUser(eq(userId), any())).thenReturn(List.of());

        mockMvc.perform(get("/status/pending/ong/{userId}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve listar usuários pendentes de um pet via GET /status/pending/user/{petId}")
    void testListPendingUsersByPetId() throws Exception {
        int petId = 88;
        PetResponseUserPendenteDTO dto = new PetResponseUserPendenteDTO(1, 2, "nomeUsuario", "emailUsuario");
        when(petStatusService.listPendingUsersByPetId(petId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/status/pending/user/{petId}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("Deve retornar 204 se não houver usuários pendentes via GET /status/pending/user/{petId}")
    void testListPendingUsersByPetId_empty() throws Exception {
        int petId = 88;
        when(petStatusService.listPendingUsersByPetId(petId)).thenReturn(List.of());

        mockMvc.perform(get("/status/pending/user/{petId}", petId))
                .andExpect(status().isNoContent());
    }

}