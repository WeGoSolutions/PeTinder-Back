package cruds.Pets.V2.infrastructure.web;

import cruds.Pets.V2.core.application.usecase.*;
import cruds.Pets.V2.infrastructure.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v2/pets")
@Tag(name = "Pet v2", description = "Endpoints Clean Architecture para gerenciamento de pets")
@Validated
public class PetController {

    private final CriarPetUseCase criarPetUseCase;
    private final BuscarPetPorIdUseCase buscarPetPorIdUseCase;
    private final ListarPetsUseCase listarPetsUseCase;
    private final AtualizarPetUseCase atualizarPetUseCase;
    private final RemoverPetUseCase removerPetUseCase;
    private final AdotarPetUseCase adotarPetUseCase;
    private final CurtirPetUseCase curtirPetUseCase;
    private final ListarPetsDisponivelParaUsuarioUseCase listarPetsDisponivelParaUsuarioUseCase;

    public PetController(CriarPetUseCase criarPetUseCase,
                         BuscarPetPorIdUseCase buscarPetPorIdUseCase,
                         ListarPetsUseCase listarPetsUseCase,
                         AtualizarPetUseCase atualizarPetUseCase,
                         RemoverPetUseCase removerPetUseCase,
                         AdotarPetUseCase adotarPetUseCase,
                         CurtirPetUseCase curtirPetUseCase,
                         ListarPetsDisponivelParaUsuarioUseCase listarPetsDisponivelParaUsuarioUseCase) {
        this.criarPetUseCase = criarPetUseCase;
        this.buscarPetPorIdUseCase = buscarPetPorIdUseCase;
        this.listarPetsUseCase = listarPetsUseCase;
        this.atualizarPetUseCase = atualizarPetUseCase;
        this.removerPetUseCase = removerPetUseCase;
        this.adotarPetUseCase = adotarPetUseCase;
        this.curtirPetUseCase = curtirPetUseCase;
        this.listarPetsDisponivelParaUsuarioUseCase = listarPetsDisponivelParaUsuarioUseCase;
    }

    @Operation(summary = "Cria um novo pet")
    @PostMapping
    public ResponseEntity<PetResponseWebDTO> criarPet(@Valid @RequestBody CriarPetWebDTO request) {
        var pet = criarPetUseCase.cadastrar(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Busca pet por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PetResponseWebDTO> buscarPetPorId(@PathVariable UUID id) {
        var pet = buscarPetPorIdUseCase.buscar(id);
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Lista todos os pets")
    @GetMapping
    public ResponseEntity<List<PetResponseWebDTO>> listarPets() {
        var pets = listarPetsUseCase.listarTodos();
        var response = pets.stream()
                .map(PetResponseWebDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista pets por ONG")
    @GetMapping("/ong/{ongId}")
    public ResponseEntity<List<PetResponseWebDTO>> listarPetsPorOng(@PathVariable UUID ongId) {
        var pets = listarPetsUseCase.listarPorOng(ongId);
        var response = pets.stream()
                .map(PetResponseWebDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista pets disponíveis para adoção")
    @GetMapping("/disponiveis")
    public ResponseEntity<List<PetResponseWebDTO>> listarPetsDisponiveis() {
        var pets = listarPetsUseCase.listarDisponiveis();
        var response = pets.stream()
                .map(PetResponseWebDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista pets disponíveis para um usuário específico")
    @GetMapping("/disponiveis/usuario/{userId}")
    public ResponseEntity<List<PetResponseWebDTO>> listarPetsDisponiveisParaUsuario(@PathVariable UUID userId) {
        var pets = listarPetsDisponivelParaUsuarioUseCase.listarDisponiveis(userId);
        var response = pets.stream()
                .map(PetResponseWebDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualiza informações do pet")
    @PutMapping("/{id}")
    public ResponseEntity<PetResponseWebDTO> atualizarPet(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarPetWebDTO request) {
        var pet = atualizarPetUseCase.atualizar(request.toCommand(id));
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Remove pet")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPet(@PathVariable UUID id) {
        removerPetUseCase.remover(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Marca pet como adotado")
    @PatchMapping("/{id}/adotar")
    public ResponseEntity<PetResponseWebDTO> adotarPet(@PathVariable UUID id) {
        var pet = adotarPetUseCase.adotar(id);
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Cancela adoção do pet")
    @PatchMapping("/{id}/cancelar-adocao")
    public ResponseEntity<PetResponseWebDTO> cancelarAdocao(@PathVariable UUID id) {
        var pet = adotarPetUseCase.cancelarAdocao(id);
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Curte um pet")
    @PatchMapping("/{id}/curtir")
    public ResponseEntity<PetResponseWebDTO> curtirPet(@PathVariable UUID id) {
        var pet = curtirPetUseCase.curtir(id);
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Remove curtida de um pet")
    @PatchMapping("/{id}/descurtir")
    public ResponseEntity<PetResponseWebDTO> descurtirPet(@PathVariable UUID id) {
        var pet = curtirPetUseCase.descurtir(id);
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }
}