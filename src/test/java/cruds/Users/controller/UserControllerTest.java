package cruds.Users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cruds.Users.controller.dto.request.*;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.repository.UserRepository;
import cruds.Users.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@WithMockUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve criar usuário com sucesso usando POST /users")
    void testCreateUser() throws Exception {
        UserRequestCriarDTO req = new UserRequestCriarDTO();
        req.setEmail("a@b.com");
        req.setSenha("Abc1@def");
        req.setNome("Nome OK");

        UserResponseCadastroDTO resp = new UserResponseCadastroDTO();
        resp.setId(1L);
        resp.setEmail(req.getEmail());
        resp.setNome(req.getNome());

        when(userService.createUser(any())).thenReturn(resp);

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("a@b.com"));
    }

    @Test
    @DisplayName("Deve autenticar usuário com sucesso usando POST /users/login")
    void testLogin() throws Exception {
        UserRequestLoginDTO req = new UserRequestLoginDTO();
        req.setEmail("x@y.com");
        req.setSenha("pass123@");

        UserResponseLoginDTO resp = UserResponseLoginDTO.builder()
                .id(2L)
                .nome("User")
                .email(req.getEmail())
                .token("tok")
                .userNovo(true)
                .build();

        when(userService.login(req.getEmail(), req.getSenha())).thenReturn(resp);

        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("tok"));
    }

    @Test
    @DisplayName("Deve atualizar informações opcionais do usuário com sucesso usando PUT /users/{id}/optional")
    void testUpdateOptional() throws Exception {
        UserRequestOptionalDTO req = new UserRequestOptionalDTO();
        req.setCep("00000");
        UserResponseCadastroDTO resp = new UserResponseCadastroDTO();
        resp.setId(3L);
        when(userService.updateOptionalInfo(eq(3), any())).thenReturn(resp);

        mockMvc.perform(put("/users/3/optional")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    @DisplayName("Deve retornar todos os usuários com sucesso usando GET /users")
    void testGetAll() throws Exception {
        UserResponseCadastroDTO u = new UserResponseCadastroDTO();
        u.setId(4L);
        when(userService.getListaUsuarios()).thenReturn(List.of(u));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(4));
    }

    @Test
    @DisplayName("Deve retornar usuário por ID com sucesso usando GET /users/{id}")
    void testGetById() throws Exception {
        UserResponseCadastroDTO u = new UserResponseCadastroDTO();
        u.setId(5L);
        when(userService.getUserById(5)).thenReturn(u);

        mockMvc.perform(get("/users/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    @DisplayName("Deve atualizar dados do usuário com sucesso usando PATCH /users/{id}")
    void testUpdateUser() throws Exception {
        UserRequestUpdateDTO req = new UserRequestUpdateDTO();
        req.setEmail("new@e.com");
        req.setSenha("Pass1@word");
        req.setNome("Nm");
        req.setDataNasc(LocalDate.from(LocalDateTime.now().minusYears(30)));
        req.setCpf("123");
        req.setCep("000");
        req.setRua("r");
        req.setNumero(1);
        req.setComplemento("c");
        req.setCidade("c");
        req.setUf("UF");

        UserResponseCadastroDTO resp = new UserResponseCadastroDTO();
        resp.setId(6L);
        when(userService.updateUser(eq(6), any())).thenReturn(resp);

        mockMvc.perform(patch("/users/6")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(6));
    }

    @Test
    @DisplayName("Deve excluir usuário com sucesso usando DELETE /users/{id}")
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(7);

        mockMvc.perform(delete("/users/7")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve enviar imagem de perfil com sucesso usando POST /users/{id}/imagem")
    void testUploadImage() throws Exception {
        byte[] imgBytes = new byte[]{1,2};
        String base = Base64.getEncoder().encodeToString(imgBytes);
        UserResponseCadastroDTO resp = new UserResponseCadastroDTO();
        resp.setId(8L);
        when(userService.uploadImagemPerfil(eq(8), any())).thenReturn(resp);

        mockMvc.perform(post("/users/8/imagem")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"imagemUsuario\":\""+base+"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8));
    }

    @Test
    @DisplayName("Deve atualizar imagem de perfil com sucesso usando PUT /users/{id}/imagem")
    void testUpdateImage() throws Exception {
        byte[] imgBytes = new byte[]{3,4};
        String base = Base64.getEncoder().encodeToString(imgBytes);
        UserResponseCadastroDTO resp = new UserResponseCadastroDTO();
        resp.setId(9L);
        when(userService.updateImagemPerfil(eq(9), any())).thenReturn(resp);

        mockMvc.perform(put("/users/9/imagem")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"imagemUsuario\":\""+base+"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9));
    }

    @Test
    @DisplayName("Deve remover imagem de perfil com sucesso usando DELETE /users/{id}/imagem")
    void testRemoveImage() throws Exception {
        UserResponseCadastroDTO resp = new UserResponseCadastroDTO();
        resp.setId(10L);
        when(userService.deleteImagemPerfil(10)).thenReturn(resp);

        mockMvc.perform(delete("/users/10/imagem")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    @DisplayName("Deve retornar imagem de perfil com sucesso usando GET /users/{userId}/imagens/{indice}")
    void testGetImageByIndex() throws Exception {
        byte[] data = new byte[]{5};
        when(userService.getImagemPorIndice(11, 0)).thenReturn(data);

        mockMvc.perform(get("/users/11/imagens/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(data));
    }

    @Test
    @DisplayName("Deve validar email existente com sucesso usando GET /users/{email}/validar-email")
    void testValidarEmail() throws Exception {
        UserResponseCadastroDTO resp = new UserResponseCadastroDTO();
        resp.setEmail("e@e.com");
        when(userService.validarEmail("e@e.com")).thenReturn(resp);

        mockMvc.perform(get("/users/e@e.com/validar-email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("e@e.com"));
    }

    @Test
    @DisplayName("Deve atualizar senha do usuário com sucesso usando PATCH /users/senha?email=...")
    void testUpdateSenha() throws Exception {
        UserRequestSenhaDTO req = new UserRequestSenhaDTO();
        req.setSenha("New1@pass");
        UserResponseCadastroDTO resp = new UserResponseCadastroDTO();
        resp.setId(12L);
        when(userService.updateSenha("u@u.com", req)).thenReturn(resp);

        mockMvc.perform(patch("/users/senha?email=u@u.com")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(12));
    }

    @Test
    @DisplayName("Deve atualizar campo userNovo para false com sucesso usando PATCH /users/{id}/user-novo")
    void testAtivarUserNovo() throws Exception {
        UserResponseCadastroDTO resp = new UserResponseCadastroDTO();
        resp.setId(13L);
        when(userService.atualizarUserNovoParaFalse(13)).thenReturn(resp);

        mockMvc.perform(patch("/users/13/user-novo")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(13));
    }

    @Test
    @DisplayName("Deve excluir todos os usuários com sucesso usando DELETE /users/teste")
    void testDeleteAll() throws Exception {
        doNothing().when(userRepository).deleteAll();

        mockMvc.perform(delete("/users/teste")
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(userRepository).deleteAll();
    }
}