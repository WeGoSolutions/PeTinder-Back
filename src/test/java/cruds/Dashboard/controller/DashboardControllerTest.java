package cruds.Dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cruds.Dashboard.controller.dto.response.DashboardResponseQuantidadePetsDTO;
import cruds.Dashboard.repository.DashboardRepository;
import cruds.Dashboard.service.DashboardService;
import cruds.Pets.controller.PetController;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.controller.dto.response.PetResponsePendenciasDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(DashboardController.class)
@WithMockUser
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DashboardService dashboardService;

    @MockitoBean
    private DashboardRepository dashboardRepository;

    @Test
    @DisplayName("Deve retornar lista de pets curtidos no ranking")
    void testListarPetsCurtidos() throws Exception {
        int ongId = 1;
        PetResponseGeralDTO dto = new PetResponseGeralDTO();
        dto.setId(10);

        Mockito.when(dashboardService.obterRankingPets(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/dashs/ranking/{ongId}", ongId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(10));
    }

    @Test
    @DisplayName("Deve retornar lista de pendências de pets da ONG")
    void testListarPendenciasPetsDaOng() throws Exception {
        int ongId = 2;
        PetResponsePendenciasDTO dto = new PetResponsePendenciasDTO();
        Mockito.when(dashboardService.listarPendenciasPetsDaOng(eq(ongId))).thenReturn(List.of(dto));

        mockMvc.perform(get("/dashs/pendencias/{ongId}", ongId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("Deve retornar estatísticas de pets adotados e não adotados")
    void testObterEstatisticasPets() throws Exception {
        int ongId = 3;
        DashboardResponseQuantidadePetsDTO dto = new DashboardResponseQuantidadePetsDTO(5, 7);

        Mockito.when(dashboardService.contarPetsAdotadosENaoAdotados(eq(ongId))).thenReturn(dto);

        mockMvc.perform(get("/dashs/adotados-ou-nao/{ongId}", ongId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.adotados").value(5))
                .andExpect(jsonPath("$.naoAdotados").value(7));
    }

}