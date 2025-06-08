package cruds.Dashboard.service;

import cruds.Dashboard.entity.Dashboard;
import cruds.Dashboard.repository.DashboardRepository;
import cruds.Ong.entity.Ong;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import cruds.Dashboard.controller.dto.response.DashboardResponseQuantidadePetsDTO;
import cruds.Pets.controller.dto.response.PetResponsePendenciasDTO;
import cruds.common.exception.NotFoundException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DashboardServiceTest {

    @InjectMocks
    private DashboardService dashboardService;

    @Mock
    private DashboardRepository dashboardRepository;

    @Mock
    private PetRepository petRepository;

    @Test
    void testObterRankingPets_success() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Dashboard dashboard = new Dashboard();
        Ong ong = new Ong();
        ong.setId(1);
        dashboard.setOng(ong);

        Pet pet = new Pet();
        pet.setId(10);
        pet.setCurtidas(5);

        when(petRepository.findByOngIdOrderByCurtidasDesc(1)).thenReturn(List.of(pet));

        List<PetResponseGeralDTO> result = dashboardService.obterRankingPets(dashboard);

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getId());
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void testObterRankingPets_notFound() {
        Dashboard dashboard = new Dashboard();
        Ong ong = new Ong();
        ong.setId(2);
        dashboard.setOng(ong);

        when(petRepository.findByOngIdOrderByCurtidasDesc(2)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> dashboardService.obterRankingPets(dashboard));
    }

    @Test
    void testListarPendenciasPetsDaOng_success() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setNome("Rex");
        pet.setIsCastrado(false);
        pet.setIsVermifugo(false);
        pet.setIsVacinado(true);

        when(petRepository.findByOngId(1)).thenReturn(List.of(pet));

        List<PetResponsePendenciasDTO> result = dashboardService.listarPendenciasPetsDaOng(1);

        assertEquals(1, result.size());
        assertEquals("Rex", result.get(0).getNome());
        assertTrue(result.get(0).getPendencias().contains("Castração"));
        assertTrue(result.get(0).getPendencias().contains("Vermífugo"));
        assertFalse(result.get(0).getPendencias().contains("Vacina"));
    }

    @Test
    void testListarPendenciasPetsDaOng_notFound() {
        when(petRepository.findByOngId(1)).thenReturn(Collections.emptyList());
        assertThrows(NotFoundException.class, () -> dashboardService.listarPendenciasPetsDaOng(1));
    }

    @Test
    void testContarPetsAdotadosENaoAdotados_success() {
        Pet pet1 = new Pet();
        pet1.setIsAdopted(true);
        Pet pet2 = new Pet();
        pet2.setIsAdopted(false);

        when(petRepository.findByOngId(1)).thenReturn(List.of(pet1, pet2));

        DashboardResponseQuantidadePetsDTO dto = dashboardService.contarPetsAdotadosENaoAdotados(1);

        assertEquals(1, dto.getAdotados());
        assertEquals(1, dto.getNaoAdotados());
    }

    @Test
    void testContarPetsAdotadosENaoAdotados_notFound() {
        when(petRepository.findByOngId(1)).thenReturn(Collections.emptyList());
        assertThrows(NotFoundException.class, () -> dashboardService.contarPetsAdotadosENaoAdotados(1));
    }

}