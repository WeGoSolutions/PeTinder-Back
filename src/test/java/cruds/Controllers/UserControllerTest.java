// File: `src/test/java/cruds/Users/Controllers/UserControllerTest.java`
package cruds.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import cruds.Users.Controllers.UserController;
import cruds.Users.Controllers.UserController;
import cruds.Users.DTOs.UserDTO;
import cruds.Users.Repositorys.UserRepository;
import cruds.Users.Tables.User;
import cruds.Users.DTOs.UserDTO;
import cruds.Users.Repositorys.UserRepository;
import cruds.Users.Tables.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO validUserDTO;
    private User userEntity;

    @BeforeEach
    void setup() {
        validUserDTO = new UserDTO();
        validUserDTO.setNome("User One");
        validUserDTO.setEmail("userone@example.com");
        validUserDTO.setSenha("123456");
        validUserDTO.setDataNasc(new Date());
        validUserDTO.setCpf("00011122233");
        validUserDTO.setCep("12345678");
        validUserDTO.setRua("Main Street");
        validUserDTO.setNumero(100);
        validUserDTO.setCidade("CityName");
        validUserDTO.setUf("UF");

        userEntity = new User();
        userEntity.setId(1);
        userEntity.setNome(validUserDTO.getNome());
        userEntity.setEmail(validUserDTO.getEmail());
        userEntity.setSenha(validUserDTO.getSenha());
        userEntity.setDataNasc(validUserDTO.getDataNasc());
        userEntity.setCpf(validUserDTO.getCpf());
        userEntity.setCep(validUserDTO.getCep());
        userEntity.setRua(validUserDTO.getRua());
        userEntity.setNumero(validUserDTO.getNumero());
        userEntity.setCidade(validUserDTO.getCidade());
        userEntity.setUf(validUserDTO.getUf());
    }

    @Test
    void testCreateUserValid() throws Exception {
        logger.info("Testando criação de usuário válido");
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userEntity.getId()))
                .andExpect(jsonPath("$.nome").value(userEntity.getNome()));
    }

    @Test
    void testCreateUserInvalid_MissingFields() throws Exception {
        logger.info("Testando criação de usuário com campos obrigatórios faltando");
        // For instance, nome is missing
        validUserDTO.setNome(null);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllUsers_Empty() throws Exception {
        logger.info("Testando listagem de usuários sem dados");
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllUsers_WithData() throws Exception {
        logger.info("Testando listagem de usuários com dados");
        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        // Note: UserController is returning status 201 when users exist.
        mockMvc.perform(get("/users"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(userEntity.getId()));
    }

    @Test
    void testGetUserById_Found() throws Exception {
        logger.info("Testando recuperação de usuário por id existente");
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userEntity.getId()));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        logger.info("Testando recuperação de usuário por id inexistente");
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUser_Found() throws Exception {
        logger.info("Testando atualização de usuário existente");
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        validUserDTO.setNome("Updated Name");
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.nome").value("Updated Name"));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        logger.info("Testando atualização de usuário inexistente");
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_Found() throws Exception {
        logger.info("Testando deleção de usuário existente");
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        logger.info("Testando deleção de usuário inexistente");
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }
}