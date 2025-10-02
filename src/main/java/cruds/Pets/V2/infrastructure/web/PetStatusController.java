package cruds.Pets.V2.infrastructure.web;

import cruds.Pets.V2.infrastructure.web.dto.PetStatusRequestWebDTO;
import cruds.Pets.V2.infrastructure.web.dto.PetStatusResponseWebDTO;
import cruds.Pets.controller.dto.response.*;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import cruds.Pets.repository.PetStatusRepository;
import cruds.Pets.service.PetStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController("petStatusControllerV2")
@RequestMapping("/v2/status")
@RequiredArgsConstructor
@Tag(name = "Pet Status v2", description = "Endpoints Clean Architecture para gerenciamento de status de pets")
public class PetStatusController {

    private final PetStatusService petStatusService;
    private final PetStatusRepository petStatusRepository;

    @Operation(summary = "Lista os pets curtidos, podendo filtrar por usuário")
    @GetMapping("/liked")
    public ResponseEntity<List<PetStatusResponseWebDTO>> listarCurtidos(@RequestParam(required = false) UUID userId) {
        List<PetStatus> statusPets = (userId == null)
                ? petStatusRepository.findAllLikedStatusPets()
                : petStatusRepository.findLikedStatusPetsByUser_Id(userId);
        if (statusPets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<PetStatusResponseWebDTO> response = statusPets.stream()
                .map(PetStatusResponseWebDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista todos os pets e o status de cada um para cada usuário")
    @GetMapping
    public ResponseEntity<List<?>> listarTodos() {
        var pets = petStatusService.getAllPetsWithUserStatus();
        if (pets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Cria ou atualiza o status de um pet para um usuário")
    @PostMapping
    public ResponseEntity<PetStatusResponseWebDTO> createOrUpdatePetStatus(@Valid @RequestBody PetStatusRequestWebDTO dto) {
        // Converter DTO da V2 para DTO da V1 para usar a mesma lógica de negócio
        cruds.Pets.controller.dto.request.PetStatusRequestDTO v1Dto = new cruds.Pets.controller.dto.request.PetStatusRequestDTO();
        v1Dto.setPetId(dto.getPetId());
        v1Dto.setUserId(dto.getUserId());
        v1Dto.setStatus(dto.getStatus());
        v1Dto.setCurtidas(dto.getCurtidas());

        var petStatus = petStatusService.createOrUpdatePetStatus(v1Dto);
        return ResponseEntity.ok(PetStatusResponseWebDTO.fromEntity(petStatus));
    }

    @Operation(summary = "Lista os pets disponíveis para um usuário que ainda não foram interagidos")
    @GetMapping("/disponivel/{userId}")
    public ResponseEntity<List<PetResponseGeralDTO>> listAvailablePetsForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(petStatusService.listAvailablePetsForUser(userId));
    }

    @Operation(summary = "Lista os pets de um usuário com um status específico")
    @GetMapping("/{userId}/{status}")
    public ResponseEntity<List<PetStatusResponseWebDTO>> getPetsByUserAndStatus(
            @PathVariable UUID userId,
            @PathVariable String status) {
        List<PetStatus> pets = petStatusService.getPetsByUserAndStatus(userId, PetStatusEnum.valueOf(status));
        List<PetStatusResponseWebDTO> response = pets.stream()
                .map(PetStatusResponseWebDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove o status de um pet para um usuário específico")
    @DeleteMapping("/{petId}/{userId}")
    public ResponseEntity<Void> deletePetStatus(
            @PathVariable UUID petId,
            @PathVariable UUID userId) {
        petStatusService.deletePetStatus(petId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista os pets com status padrão para ONGs")
    @GetMapping("/default/{userId}")
    public ResponseEntity<List<PetResponseGeralDTO>> listDefaultPets(@PathVariable UUID userId) {
        var response = petStatusService.listDefaultPets(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Define o status de um pet como LIKED para um usuário")
    @PostMapping("/liked/{petId}/{userId}")
    public ResponseEntity<?> setLikedStatus(
            @PathVariable UUID petId,
            @PathVariable UUID userId) {

        var existingStatusOpt = petStatusRepository.findByPetIdAndUserId(petId, userId);

        if (existingStatusOpt.isPresent() && existingStatusOpt.get().getStatus() == PetStatusEnum.LIKED) {
            petStatusService.decrementarCurtidasPet(petId);
            petStatusService.deletePetStatus(petId, userId);
            return ResponseEntity.noContent().build();
        }

        PetStatusRequestWebDTO dto = new PetStatusRequestWebDTO();
        dto.setPetId(petId);
        dto.setUserId(userId);
        dto.setStatus(PetStatusEnum.LIKED);

        petStatusService.incrementarCurtidasPet(petId);

        // Converter para DTO V1 para usar a mesma lógica
        cruds.Pets.controller.dto.request.PetStatusRequestDTO v1Dto = new cruds.Pets.controller.dto.request.PetStatusRequestDTO();
        v1Dto.setPetId(dto.getPetId());
        v1Dto.setUserId(dto.getUserId());
        v1Dto.setStatus(dto.getStatus());

        var petStatus = petStatusService.createOrUpdatePetStatus(v1Dto);
        return ResponseEntity.ok(PetStatusResponseWebDTO.fromEntity(petStatus));
    }

    @Operation(summary = "Define o status de um pet como ADOPTED para um usuário")
    @PostMapping("/adopted/{petId}/{userId}")
    public ResponseEntity<PetStatusResponseWebDTO> adoptPet(
            @PathVariable UUID petId,
            @PathVariable UUID userId) {

        PetStatus petStatus = petStatusService.adoptPet(petId, userId);
        return ResponseEntity.ok(PetStatusResponseWebDTO.fromEntity(petStatus));
    }

    @Operation(summary = "Define o status de um pet como PENDING para um usuário")
    @PostMapping("/pending/{petId}/{userId}")
    public ResponseEntity<PetStatusResponseWebDTO> setPendingStatus(
            @PathVariable UUID petId,
            @PathVariable UUID userId) {

        PetStatusRequestWebDTO dto = new PetStatusRequestWebDTO();
        dto.setPetId(petId);
        dto.setUserId(userId);
        dto.setStatus(PetStatusEnum.PENDING);
        dto.getAlteradoParaPending();

        // Converter para DTO V1 para usar a mesma lógica
        cruds.Pets.controller.dto.request.PetStatusRequestDTO v1Dto = new cruds.Pets.controller.dto.request.PetStatusRequestDTO();
        v1Dto.setPetId(dto.getPetId());
        v1Dto.setUserId(dto.getUserId());
        v1Dto.setStatus(dto.getStatus());

        var petStatus = petStatusService.createOrUpdatePetStatus(v1Dto);
        return ResponseEntity.ok(PetStatusResponseWebDTO.fromEntity(petStatus));
    }

    @Operation(summary = "Lista todos os status de um pet específico")
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<PetStatusEnum>> getPetStatusList(@PathVariable UUID petId) {
        List<PetStatusEnum> statusList = petStatusService.getPetStatusList(petId);
        if (statusList.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(statusList);
    }

    @Operation(summary = "Lista os pets com status PENDING com informações das ONGs para um usuário específico")
    @GetMapping("/pending/ong/{userId}")
    public ResponseEntity<List<PetResponsePendingOngDTO>> listPendingPetsWithOngForUser(
            HttpServletRequest request,
            @PathVariable UUID userId) {
        List<PetResponsePendingOngDTO> pendingPets = petStatusService.listPendingPetsWithOngForUser(userId, request);
        if (pendingPets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(pendingPets);
    }

    @Operation(summary = "Lista os usuarios que estao com o status PENDING de um pet especifico")
    @GetMapping("/pending/user/{petId}")
    public ResponseEntity<List<PetResponseUserPendenteDTO>> listPendingUsersByPetId(@PathVariable UUID petId) {
        List<PetResponseUserPendenteDTO> userIds = petStatusService.listPendingUsersByPetId(petId);
        if (userIds.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(userIds);
    }

    @Operation(summary = "Pega todas as informações do pet e do adotante")
    @GetMapping("/adopted/{petId}")
    public ResponseEntity<PetResponseAdotanteDTO> getAdoptedInfoByPetId(@PathVariable UUID petId) {
        PetResponseAdotanteDTO dto = petStatusService.getAdoptedInfoByPetId(petId);
        return ResponseEntity.ok(dto);
    }
}
