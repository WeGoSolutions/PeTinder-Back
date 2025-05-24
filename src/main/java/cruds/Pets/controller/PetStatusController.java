package cruds.Pets.controller;

import cruds.Pets.controller.dto.request.PetStatusRequestDTO;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.controller.dto.response.PetStatusResponseDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import cruds.Pets.repository.PetStatusRepository;
import cruds.Pets.service.PetStatusService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class PetStatusController {

    private final PetStatusRepository statusPetRepository;
    private final PetStatusService petStatusService;

    @GetMapping
    public ResponseEntity<List<PetStatusResponseDTO>> listar() {
        List<PetStatus> statusPets = statusPetRepository.findAll();
        if (statusPets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<PetStatusResponseDTO> response = statusPets.stream()
                .map(PetStatusResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/liked")
    public ResponseEntity<List<PetStatus>> curtidos(@RequestParam(required = false) Integer usuarioId) {
        List<PetStatus> statusPetsCurtidos = (usuarioId == null)
                ? statusPetRepository.findAllLikedStatusPets()
                : statusPetRepository.findLikedStatusPetsByUser_Id(usuarioId);

        if (statusPetsCurtidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(statusPetsCurtidos);
    }

    @Operation(summary = "Cria ou atualiza o status de um pet para um usuário")
    @PostMapping
    public ResponseEntity<PetStatusResponseDTO> createOrUpdatePetStatus(@Valid @RequestBody PetStatusRequestDTO dto) {
        var petStatus = petStatusService.createOrUpdatePetStatus(dto);
        return ResponseEntity.ok(new PetStatusResponseDTO(petStatus));
    }

    @Operation(summary = "Lista pets disponíveis para um usuário (sem interação prévia)")
    @GetMapping("/disponivel/{userId}")
    public ResponseEntity<List<PetResponseGeralDTO>> listAvailablePetsForUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(petStatusService.listAvailablePetsForUser(userId));
    }

    @Operation(summary = "Lista pets com status específico para um usuário")
    @GetMapping("/{userId}/{status}")
    public ResponseEntity<List<PetStatus>> getPetsByUserAndStatus(
            @PathVariable Integer userId,
            @PathVariable PetStatusEnum status) {
        return ResponseEntity.ok(petStatusService.getPetsByUserAndStatus(userId, status));
    }

    @Operation(summary = "Remove o status de um pet para um usuário")
    @DeleteMapping("/{petId}/{userId}")
    public ResponseEntity<Void> deletePetStatus(
            @PathVariable Integer petId,
            @PathVariable Integer userId) {
        petStatusService.deletePetStatus(petId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista os pets com status default para ONGs")
    @GetMapping("/default/{userId}")
    public ResponseEntity<List<PetResponseGeralDTO>> listDefaultPets(@PathVariable Integer userId) {
        List<PetResponseGeralDTO> response = petStatusService.listDefaultPets(userId);
        return ResponseEntity.ok(response);
    }
}