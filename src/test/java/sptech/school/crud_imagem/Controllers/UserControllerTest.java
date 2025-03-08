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
import sptech.school.crud_imagem.DTOs.UserDTO;
import sptech.school.crud_imagem.Tables.User;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Order(1)
    public void testCreateUser() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setNome("Teste");
        dto.setEmail("teste@exemplo.com");
        dto.setSenha("123456");
        dto.setDataNasc(new Date());
        dto.setCpf("12345678900");
        dto.setCep("00000000");
        dto.setRua("Rua Teste");
        dto.setNumero(100);
        dto.setCidade("Cidade");
        dto.setUf("UF");

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @Order(2)
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().is(201));
    }

    @Test
    @Order(3)
    public void testGetUserById() throws Exception {
        // Primeiro, crie um usuário para obter o id
        UserDTO dto = new UserDTO();
        dto.setNome("UsuarioId");
        dto.setEmail("usuarioid@exemplo.com");
        dto.setSenha("senha");
        dto.setDataNasc(new Date());
        dto.setCpf("11122233344");
        dto.setCep("11111111");
        dto.setRua("Rua Id");
        dto.setNumero(101);
        dto.setCidade("CidadeID");
        dto.setUf("UF");

        String content = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        User savedUser = mapper.readValue(content, User.class);

        mockMvc.perform(get("/user/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("UsuarioId"));
    }

    @Test
    @Order(4)
    public void testUpdateUser() throws Exception {
        // Cria usuário
        UserDTO dto = new UserDTO();
        dto.setNome("Atualizar");
        dto.setEmail("atualizar@exemplo.com");
        dto.setSenha("123");
        dto.setDataNasc(new Date());
        dto.setCpf("22233344455");
        dto.setCep("22222222");
        dto.setRua("Rua Atual");
        dto.setNumero(102);
        dto.setCidade("CidadeAtual");
        dto.setUf("UF");

        String content = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        User savedUser = mapper.readValue(content, User.class);

        // Atualiza o usuário
        dto.setNome("Atualizado");
        mockMvc.perform(put("/user/" + savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.nome").value("Atualizado"));
    }

    @Test
    @Order(5)
    public void testDeleteUser() throws Exception {
        // Cria usuário
        UserDTO dto = new UserDTO();
        dto.setNome("Excluir");
        dto.setEmail("excluir@exemplo.com");
        dto.setSenha("123");
        dto.setDataNasc(new Date());
        dto.setCpf("33344455566");
        dto.setCep("33333333");
        dto.setRua("Rua Excluir");
        dto.setNumero(103);
        dto.setCidade("CidadeExcluir");
        dto.setUf("UF");

        String content = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        User savedUser = mapper.readValue(content, User.class);

        // Deleta o usuário
        mockMvc.perform(delete("/user/" + savedUser.getId()))
                .andExpect(status().isNoContent());
    }
}