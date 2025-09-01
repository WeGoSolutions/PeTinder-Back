package cruds.Pets.service;

import cruds.Ong.controller.dto.response.OngResponseDTO;
import cruds.Ong.entity.Ong;
import cruds.Pets.controller.dto.request.PetStatusRequestDTO;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.controller.dto.response.PetResponsePendingOngDTO;
import cruds.Pets.controller.dto.response.PetResponseUserPendenteDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import cruds.Pets.repository.PetRepository;
import cruds.Pets.repository.PetStatusRepository;
import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PetStatusService {

    @Autowired
    private PetStatusRepository petStatusRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PetStatus createOrUpdatePetStatus(PetStatusRequestDTO dto) {
        PetStatus status = petStatusRepository.findByPet_IdAndUser_Id(dto.getPetId(), dto.getUserId())
                .orElse(new PetStatus());

        status.setStatus(dto.getStatus());

        if (dto.getStatus() == PetStatusEnum.PENDING) {
            status.setAlteradoParaPending(java.time.LocalDateTime.now());
        }

        if (status.getId() == null) {
            Pet pet = petRepository.findById(dto.getPetId())
                    .orElseThrow(() -> new EntityNotFoundException("Pet não encontrado"));
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

            status.setPet(pet);
            status.setUser(user);
        }
        return petStatusRepository.save(status);
    }

    public List<PetStatusEnum> getPetStatusList(UUID petId) {
        List<PetStatus> statuses = petStatusRepository.findByPet_Id(petId);
        List<PetStatusEnum> statusList = statuses.stream()
                .map(PetStatus::getStatus)
                .collect(Collectors.toList());

        if (statusList.isEmpty()) {
            statusList.add(PetStatusEnum.ADOPTED);
        }

        return statusList;
    }

    public List<PetResponsePendingOngDTO> listPendingPetsWithOngForUser(UUID userId, HttpServletRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Usuário com id " + userId + " não encontrado");
        }

        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        List<PetStatus> pendingPets = petStatusRepository.findByUserIdAndStatus(userId, PetStatusEnum.PENDING);

        return pendingPets.stream()
                .map(petStatus -> {
                    Pet pet = petStatus.getPet();
                    Ong ong = pet.getOng();

                    List<String> imageUrls = new ArrayList<>();
                    for (int i = 0; i < pet.getImagens().size(); i++) {
                        imageUrls.add(baseUrl + "/pets/" + pet.getId() + "/imagens/" + i);
                    }

                    PetResponsePendingOngDTO dto = new PetResponsePendingOngDTO(
                            userId,
                            pet.getId(),
                            pet.getNome(),
                            pet.getIdade(),
                            pet.getPorte(),
                            pet.getDescricao(),
                            pet.getIsCastrado(),
                            pet.getIsVermifugo(),
                            pet.getIsVacinado(),
                            imageUrls,
                            pet.getSexo(),
                            ong.getId(),
                            OngResponseDTO.toResponse(ong)
                    );

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<PetResponseGeralDTO> listAvailablePetsForUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Usuário com id " + userId + " não encontrado");
        }
        return petStatusRepository.findPetsNotInteractedByUser(userId)
                .stream()
                .filter((Pet pet) -> pet.getIsAdopted() == null || !pet.getIsAdopted())
                .map(PetResponseGeralDTO::toResponse)
                .collect(Collectors.toList());
    }

    public List<PetStatus> getPetsByUserAndStatus(UUID userId, PetStatusEnum status) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Usuário com id " + userId + " não encontrado");
        }

        return petStatusRepository.findByUserIdAndStatus(userId, status);
    }

    public void deletePetStatus(UUID petId, UUID userId) {
        PetStatus status = petStatusRepository.findByPetIdAndUserId(petId, userId)
                .orElseThrow(() -> new NotFoundException("Status não encontrado para pet " + petId + " e usuário " + userId));

        petStatusRepository.delete(status);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException("Pet não encontrado com id " + petId));
        pet.setIsAdopted(false);
        petRepository.save(pet);
    }

    public List<PetResponseGeralDTO> listDefaultPets(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Usuário com id " + userId + " não encontrado");
        }
        List<PetResponseGeralDTO> availablePets = petStatusRepository
                .findPetsNotInteractedByUser(userId)
                .stream()
                .filter((Pet pet) -> pet.getIsAdopted() == null || !pet.getIsAdopted())
                .map(PetResponseGeralDTO::toResponse)
                .collect(Collectors.toList());

        if (availablePets.isEmpty()) {
            throw new NotFoundException("Nenhum pet default encontrado para o usuário " + userId);
        }
        return availablePets;
    }

    public void incrementarCurtidasPet(UUID petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet não encontrado"));
        int atual = pet.getCurtidas() != null ? pet.getCurtidas() : 0;
        pet.setCurtidas(atual + 1);
        petRepository.save(pet);
    }

    public void decrementarCurtidasPet(UUID petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet não encontrado"));
        int atual = pet.getCurtidas() != null ? pet.getCurtidas() : 0;
        pet.setCurtidas(atual - 1);
        petRepository.save(pet);
    }

    public void publicarAdocao(UUID petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet não encontrado"));
        pet.setIsAdopted(true);
        petRepository.save(pet);
    }

    @Transactional
    public PetStatus adoptPet(UUID petId, UUID userId) {
        petStatusRepository.deleteByPetIdAndUserIdNot(petId, userId);

        Optional<PetStatus> optionalStatus = petStatusRepository.findByPet_IdAndUser_Id(petId, userId);
        PetStatus petStatus;

        if (optionalStatus.isPresent()) {
            petStatus = optionalStatus.get();
            petStatus.setStatus(PetStatusEnum.ADOPTED);
        } else {
            petStatus = new PetStatus();

            Pet pet = petRepository.findById(petId)
                    .orElseThrow(() -> new EntityNotFoundException("Pet não encontrado"));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

            petStatus.setPet(pet);
            petStatus.setUser(user);
            petStatus.setStatus(PetStatusEnum.ADOPTED);
        }

        Pet pet = petStatus.getPet();
        if (pet == null) {
            pet = petRepository.findById(petId)
                    .orElseThrow(() -> new EntityNotFoundException("Pet não encontrado"));
            petStatus.setPet(pet);
        }
        pet.setIsAdopted(true);
        petRepository.save(pet);

        return petStatusRepository.save(petStatus);
    }

    public List<Object> getAllPetsWithUserStatus() {
        List<Pet> pets = petRepository.findAll();
        List<User> users = userRepository.findAll();
        List<Object> result = new java.util.ArrayList<>();
        for (Pet pet : pets) {
            for (User user : users) {
                PetStatus status = petStatusRepository.findByPet_IdAndUser_Id(pet.getId(), user.getId()).orElse(null);
                String statusStr = (status != null && status.getStatus() != null)
                        ? status.getStatus().toString()
                        : null;

                Map<String, Object> map = new HashMap<>();
                map.put("id_pet", pet.getId().toString());   // UUID convertido para String
                map.put("id_user", user.getId().toString()); // UUID convertido para String
                map.put("nome_pet", pet.getNome());
                map.put("status", statusStr);
                result.add(map);
            }
        }
        return result;
    }

    @Transactional
    public void removerOutrosStatus(UUID petId, UUID userId) {
        petStatusRepository.deleteByPetIdAndUserIdNot(petId, userId);
    }

    public List<PetResponseUserPendenteDTO> listPendingUsersByPetId(UUID petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException("Pet com id " + petId + " não encontrado"));

        List<PetStatus> pendingStatuses = petStatusRepository.findByPet_IdAndStatus(petId, PetStatusEnum.PENDING);

        if (pendingStatuses.isEmpty()) {
            throw new NotFoundException("Nenhum usuário pendente encontrado para o pet com id " + petId);
        }

        return pendingStatuses.stream()
                .map(status -> PetResponseUserPendenteDTO.toResponse(pet, status.getUser()))
                .collect(Collectors.toList());
    }
}
