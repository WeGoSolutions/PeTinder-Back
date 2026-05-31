package cruds.DashboardGlobal.infrastructure.web;

import cruds.DashboardGlobal.core.application.usecase.ObterDashboardGlobalUseCase;
import cruds.DashboardGlobal.infrastructure.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashs/global")
@Tag(name = "Dashboard Global", description = "Endpoints para Dashboard com dados globais de abrigos")
public class DashboardGlobalController {

    private final ObterDashboardGlobalUseCase useCase;

    public DashboardGlobalController(ObterDashboardGlobalUseCase useCase) {
        this.useCase = useCase;
    }

    @Operation(summary = "Retorna os KPIs globais do dashboard")
    @GetMapping("/kpis")
    public ResponseEntity<DashboardGlobalKpisResponseDTO> obterKpis() {
        var kpis = useCase.obterKpis();
        return ResponseEntity.ok(DashboardGlobalKpisResponseDTO.fromDomain(kpis));
    }

    @Operation(summary = "Retorna top 5 especies por quantidade de entrada")
    @GetMapping("/entradas-por-especie")
    public ResponseEntity<List<EspecieContagemResponseDTO>> obterEntradasPorEspecie() {
        var result = useCase.obterEntradasPorEspecie().stream()
                .map(EspecieContagemResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Retorna distribuicao por status final dos animais")
    @GetMapping("/distribuicao-status")
    public ResponseEntity<List<StatusContagemResponseDTO>> obterDistribuicaoStatus() {
        var result = useCase.obterDistribuicaoStatus().stream()
                .map(StatusContagemResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Retorna top 5 motivos de retorno/devolucao")
    @GetMapping("/motivos-retorno")
    public ResponseEntity<List<MotivoRetornoContagemResponseDTO>> obterMotivosRetorno() {
        var result = useCase.obterMotivosRetorno().stream()
                .map(MotivoRetornoContagemResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Retorna distribuicao por faixa etaria")
    @GetMapping("/distribuicao-faixa-etaria")
    public ResponseEntity<List<FaixaEtariaContagemResponseDTO>> obterDistribuicaoFaixaEtaria() {
        var result = useCase.obterDistribuicaoFaixaEtaria().stream()
                .map(FaixaEtariaContagemResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
