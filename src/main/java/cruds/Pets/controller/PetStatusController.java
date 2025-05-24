package cruds.Pets.controller;

import cruds.Pets.controller.dto.request.PetStatusRequestDTO;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.controller.dto.response.PetStatusResponseDTO;
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

    private final PetStatusService petStatusService;
    private final PetStatusRepository petStatusRepository;

    @Operation(summary = "Lista os pets curtidos, podendo filtrar por usuário")
    @GetMapping("/liked")
    public ResponseEntity<List<PetStatusResponseDTO>> listarCurtidos(@RequestParam(required = false) Integer userId) {
        List<PetStatus> statusPets = (userId == null)
                ? petStatusRepository.findAllLikedStatusPets()
                : petStatusRepository.findLikedStatusPetsByUser_Id(userId);
        if (statusPets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<PetStatusResponseDTO> response = statusPets.stream()
                .map(PetStatusResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista todos os status de todos os pets")
    @GetMapping
    public ResponseEntity<List<PetStatusResponseDTO>> listarTodos(@RequestParam(required = false) Integer userId) {
        List<PetStatus> statusPets = (userId == null)
                ? petStatusRepository.findAllLikedStatusPets()
                : petStatusRepository.findLikedStatusPetsByUser_Id(userId);
        if (statusPets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<PetStatusResponseDTO> response = statusPets.stream()
                .map(PetStatusResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cria ou atualiza o status de um pet para um usuário")
    @PostMapping
    public ResponseEntity<PetStatusResponseDTO> createOrUpdatePetStatus(@Valid @RequestBody PetStatusRequestDTO dto) {
        var petStatus = petStatusService.createOrUpdatePetStatus(dto);
        return ResponseEntity.ok(new PetStatusResponseDTO(petStatus));
    }

    @Operation(summary = "Lista os pets disponíveis para um usuário que ainda não foram interagidos")
    @GetMapping("/disponivel/{userId}")
    public ResponseEntity<List<PetResponseGeralDTO>> listAvailablePetsForUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(petStatusService.listAvailablePetsForUser(userId));
    }

    @Operation(summary = "Lista os pets de um usuário com um status específico")
    @GetMapping("/{userId}/{status}")
    public ResponseEntity<List<PetStatusResponseDTO>> getPetsByUserAndStatus(
            @PathVariable Integer userId,
            @PathVariable String status) {
        List<PetStatus> pets = petStatusService.getPetsByUserAndStatus(userId, PetStatusEnum.valueOf(status));
        List<PetStatusResponseDTO> response = pets.stream()
                .map(PetStatusResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove o status de um pet para um usuário específico")
    @DeleteMapping("/{petId}/{userId}")
    public ResponseEntity<Void> deletePetStatus(
            @PathVariable Integer petId,
            @PathVariable Integer userId) {
        petStatusService.deletePetStatus(petId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista os pets com status padrão para ONGs")
    @GetMapping("/default/{userId}")
    public ResponseEntity<List<PetResponseGeralDTO>> listDefaultPets(@PathVariable Integer userId) {
        var response = petStatusService.listDefaultPets(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Define o status de um pet como LIKED para um usuário")
    @PostMapping("/liked/{petId}/{userId}")
    public ResponseEntity<PetStatusResponseDTO> setLikedStatus(
            @PathVariable Integer petId,
            @PathVariable Integer userId) {
        PetStatusRequestDTO dto = new PetStatusRequestDTO();
        dto.setPetId(petId);
        dto.setUserId(userId);
        dto.setStatus(PetStatusEnum.valueOf("LIKED"));

        petStatusService.incrementarCurtidasPet(petId);

        var petStatus = petStatusService.createOrUpdatePetStatus(dto);
        return ResponseEntity.ok(new PetStatusResponseDTO(petStatus));
    }

    @Operation(summary = "Define o status de um pet como ADOPTED para um usuário")
    @PostMapping("/adopted/{petId}/{userId}")
    public ResponseEntity<PetStatusResponseDTO> setAdoptedStatus(
            @PathVariable Integer petId,
            @PathVariable Integer userId) {

        petStatusService.publicarAdocao(petId);

        PetStatusRequestDTO dto = new PetStatusRequestDTO();
        dto.setPetId(petId);
        dto.setUserId(userId);
        dto.setStatus(PetStatusEnum.ADOPTED);

        var petStatus = petStatusService.createOrUpdatePetStatus(dto);
        return ResponseEntity.ok(new PetStatusResponseDTO(petStatus));
    }
}