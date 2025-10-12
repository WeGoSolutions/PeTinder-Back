package cruds.Dashboard.V2.infrastructure.web;

import cruds.Dashboard.V2.core.application.usecase.ListarPendenciasPetsUseCase;
import cruds.Dashboard.V2.core.application.usecase.ObterEstatisticasPetsUseCase;
import cruds.Dashboard.V2.core.application.usecase.ObterRankingPetsUseCase;
import cruds.Dashboard.V2.infrastructure.web.dto.DashboardEstatisticasResponseWebDTO;
import cruds.Dashboard.V2.infrastructure.web.dto.PetPendenciaResponseWebDTO;
import cruds.Dashboard.V2.infrastructure.web.dto.PetRankingResponseWebDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController("dashboardControllerV2")
@RequestMapping("/api/dashs")
@Tag(name = "Dashboard v2", description = "Endpoints Clean Architecture para Dashboard")
@Validated
public class DashboardController {

    private final ObterRankingPetsUseCase obterRankingPetsUseCase;
    private final ListarPendenciasPetsUseCase listarPendenciasPetsUseCase;
    private final ObterEstatisticasPetsUseCase obterEstatisticasPetsUseCase;

    public DashboardController(ObterRankingPetsUseCase obterRankingPetsUseCase,
                              ListarPendenciasPetsUseCase listarPendenciasPetsUseCase,
                              ObterEstatisticasPetsUseCase obterEstatisticasPetsUseCase) {
        this.obterRankingPetsUseCase = obterRankingPetsUseCase;
        this.listarPendenciasPetsUseCase = listarPendenciasPetsUseCase;
        this.obterEstatisticasPetsUseCase = obterEstatisticasPetsUseCase;
    }

    @Operation(summary = "Lista pets mais curtidos da ONG")
    @GetMapping("/ranking/{ongId}")
    public ResponseEntity<List<PetRankingResponseWebDTO>> listarPetsCurtidos(@PathVariable UUID ongId) {
        var pets = obterRankingPetsUseCase.obterRanking(ongId);
        var response = pets.stream()
                .map(PetRankingResponseWebDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista pendências dos pets da ONG")
    @GetMapping("/pendencias/{ongId}")
    public ResponseEntity<List<PetPendenciaResponseWebDTO>> listarPendenciasPetsDaOng(@PathVariable UUID ongId) {
        var pets = listarPendenciasPetsUseCase.listarPendencias(ongId);
        var response = pets.stream()
                .map(PetPendenciaResponseWebDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna estatísticas de pets adotados e não adotados")
    @GetMapping("/adotados-ou-nao/{ongId}")
    public ResponseEntity<DashboardEstatisticasResponseWebDTO> obterEstatisticasPets(@PathVariable UUID ongId) {
        var estatisticas = obterEstatisticasPetsUseCase.obterEstatisticas(ongId);
        return ResponseEntity.ok(DashboardEstatisticasResponseWebDTO.fromDomain(estatisticas));
    }
}

