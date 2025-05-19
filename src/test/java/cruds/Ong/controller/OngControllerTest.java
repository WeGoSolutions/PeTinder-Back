package cruds.Ong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cruds.Ong.controller.dto.request.OngRequestCriarDTO;
import cruds.Ong.controller.dto.request.OngRequestImagemDTO;
import cruds.Ong.controller.dto.request.OngRequestLoginDTO;
import cruds.Ong.controller.dto.request.OngRequestUpdateDTO;
import cruds.Ong.controller.dto.response.OngResponseDTO;
import cruds.Ong.controller.dto.response.OngResponseLoginDTO;
import cruds.Ong.entity.Ong;
import cruds.Ong.repository.OngRepository;
import cruds.Ong.service.OngService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OngController.class)
@WithMockUser
class OngControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OngService ongService;

    @MockitoBean
    private OngRepository ongRepository;

    @Test
    @DisplayName("Deve criar ONG com sucesso usando POST /ong")
    void testCriarOng() throws Exception {
        OngRequestCriarDTO req = new OngRequestCriarDTO();
        req.setEmail("ong@teste.com");
        req.setSenha("Senha@123");
        req.setNome("ONG Teste");
        req.setCnpj("12.345.678/0001-90");

        Ong ongCriada = new Ong();
        ongCriada.setId(1);
        ongCriada.setEmail(req.getEmail());
        ongCriada.setNome(req.getNome());
        ongCriada.setCnpj(req.getCnpj());

        when(ongService.criarOng(any())).thenReturn(ongCriada);

        mockMvc.perform(post("/ong")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("ong@teste.com"))
                .andExpect(jsonPath("$.nome").value("ONG Teste"));
    }

    @Test
    @DisplayName("Deve autenticar ONG com sucesso usando POST /ong/login")
    void testLogin() throws Exception {
        OngRequestLoginDTO req = new OngRequestLoginDTO();
        req.setEmail("ong@teste.com");
        req.setSenha("Senha@123");

        OngResponseLoginDTO resp = OngResponseLoginDTO.builder()
                .id(1)
                .nome("ONG Teste")
                .email("ong@teste.com")
                .build();

        when(ongService.login(eq(req.getEmail()), eq(req.getSenha()))).thenReturn(resp);

        mockMvc.perform(post("/ong/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("ONG Teste"))
                .andExpect(jsonPath("$.email").value("ong@teste.com"));
    }

    @Test
    @DisplayName("Deve retornar ONG por ID usando GET /ong/{id}")
    void testGetOng() throws Exception {
        OngResponseDTO resp = new OngResponseDTO();
        resp.setId(1);
        resp.setNome("ONG Teste");
        resp.setEmail("ong@teste.com");
        resp.setCnpj("12.345.678/0001-90");

        when(ongService.getOng(1)).thenReturn(resp);

        mockMvc.perform(get("/ong/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("ONG Teste"))
                .andExpect(jsonPath("$.email").value("ong@teste.com"))
                .andExpect(jsonPath("$.cnpj").value("12.345.678/0001-90"));
    }

    @Test
    @DisplayName("Deve atualizar ONG com sucesso usando PATCH /ong/{id}")
    void testUpdateOng() throws Exception {
        OngRequestUpdateDTO req = new OngRequestUpdateDTO();
        req.setEmail("atualizado@teste.com");
        req.setNome("ONG Atualizada");
        req.setCnpj("12.345.678/0001-90");
        req.setSenha("NovaSenha@123");

        OngResponseDTO resp = new OngResponseDTO();
        resp.setId(1);
        resp.setNome(req.getNome());
        resp.setEmail(req.getEmail());
        resp.setCnpj(req.getCnpj());

        when(ongService.updateOng(eq(1), any(OngRequestUpdateDTO.class))).thenReturn(resp);

        mockMvc.perform(patch("/ong/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("ONG Atualizada"))
                .andExpect(jsonPath("$.email").value("atualizado@teste.com"));
    }

    @Test
    @DisplayName("Deve atualizar imagem da ONG com sucesso usando POST /ong/{id}")
    void testUpdateImageOng() throws Exception {
        OngRequestImagemDTO req = new OngRequestImagemDTO();
        req.setNomeArquivo("imagem.jpg");
        req.setImagensBytes(Base64.getEncoder().encode("dados-imagem".getBytes()));

        Ong ongAtualizada = new Ong();
        ongAtualizada.setId(1);
        ongAtualizada.setNome("ONG Teste");
        ongAtualizada.setEmail("ong@teste.com");

        when(ongService.updateImageOng(eq(1), eq(req.getNomeArquivo()), eq(req.getImagensBytes())))
                .thenReturn(ongAtualizada);

        mockMvc.perform(post("/ong/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("ONG Teste"))
                .andExpect(jsonPath("$.email").value("ong@teste.com"));
    }

    @Test
    @DisplayName("Deve retornar imagem da ONG com sucesso usando GET /ong/{id}/imagem")
    void testGetImageOng() throws Exception {
        OngResponseDTO resp = new OngResponseDTO();
        resp.setId(1);
        resp.setNome("ONG Teste");
        resp.setEmail("ong@teste.com");

        when(ongService.getImageOng(1)).thenReturn(resp);

        mockMvc.perform(get("/ong/1/imagem")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("ONG Teste"))
                .andExpect(jsonPath("$.email").value("ong@teste.com"));
    }
}