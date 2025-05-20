package cruds.Dashboard.controller;

import cruds.Dashboard.controller.dto.response.DashboardResponseQuantidadePetsDTO;
import cruds.Dashboard.entity.Dashboard;
import cruds.Dashboard.service.DashboardService;
import cruds.Ong.entity.Ong;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.controller.dto.response.PetResponsePendenciasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashs")
@Validated
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @GetMapping("/ranking/{ongId}")
    public ResponseEntity<List<PetResponseGeralDTO>> listarPetsCurtidos(@PathVariable Integer ongId){
        Dashboard dashboard = new Dashboard();
        Ong ong = new Ong();
        ong.setId(ongId);
        dashboard.setOng(ong);

        List<PetResponseGeralDTO> petsCurtidos = dashboardService.obterRankingPets(dashboard);
        return ResponseEntity.ok(petsCurtidos);
    }

    @GetMapping("/pendencias/{ongId}")
    public ResponseEntity<List<PetResponsePendenciasDTO>> listarPendenciasPetsDaOng(@PathVariable Integer ongId) {
        List<PetResponsePendenciasDTO> pendencias = dashboardService.listarPendenciasPetsDaOng(ongId);
        return ResponseEntity.ok(pendencias);
    }

    @GetMapping("/adotados-ou-nao/{ongId}")
    public ResponseEntity<DashboardResponseQuantidadePetsDTO> obterEstatisticasPets(@PathVariable Integer ongId) {
        DashboardResponseQuantidadePetsDTO estatisticas = dashboardService.contarPetsAdotadosENaoAdotados(ongId);
        return ResponseEntity.ok(estatisticas);
    }
}
