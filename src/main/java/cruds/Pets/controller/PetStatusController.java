package cruds.Pets.controller;

import cruds.Pets.controller.dto.request.PetStatusRequestDTO;
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

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class PetStatusController {

    private final PetStatusRepository statusPetRepository;
    private final PetStatusService petStatusService;

    @GetMapping
    public ResponseEntity<List<PetStatus>> listar() {
        List<PetStatus> statusPets = statusPetRepository.findAll();

        if (statusPets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(statusPets);
    }

    @GetMapping("/liked")
    public ResponseEntity<List<PetStatus>> curtidos() {
        List<PetStatus> statusPetsCurtidos = statusPetRepository.findAllLikedStatusPets();

        if (statusPetsCurtidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(statusPetsCurtidos);
    }

    @GetMapping("/liked/{usuarioId}")
    public ResponseEntity<List<PetStatus>> curtidosPorUsuario(@PathVariable Integer usuarioId) {
        List<PetStatus> statusPetsCurtidos = statusPetRepository.findLikedStatusPetsByUser_Id(usuarioId);

        if (statusPetsCurtidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(statusPetsCurtidos);
    }

    @Operation(summary = "Cria ou atualiza o status de um pet para um usuário")
    @PostMapping
    public ResponseEntity<PetStatus> createOrUpdatePetStatus(@Valid @RequestBody PetStatusRequestDTO dto) {
        return ResponseEntity.ok(petStatusService.createOrUpdatePetStatus(dto));
    }

    @Operation(summary = "Lista pets disponíveis para um usuário (sem interação prévia)")
    @GetMapping("/disponivel/{userId}")
    public ResponseEntity<List<Pet>> listAvailablePetsForUser(@PathVariable Integer userId) {
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
}
