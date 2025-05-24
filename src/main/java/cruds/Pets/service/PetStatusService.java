package cruds.Pets.service;

import cruds.Pets.controller.dto.request.PetStatusRequestDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import cruds.Pets.repository.PetRepository;
import cruds.Pets.repository.PetStatusRepository;
import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetStatusService {

    @Autowired
    private PetStatusRepository petStatusRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    public PetStatus createOrUpdatePetStatus(PetStatusRequestDTO dto) {
        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new NotFoundException("Pet com id " + dto.getPetId() + " não encontrado"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("Usuário com id " + dto.getUserId() + " não encontrado"));

        Optional<PetStatus> existingStatus = petStatusRepository.findByPetIdAndUserId(dto.getPetId(), dto.getUserId());

        if (existingStatus.isPresent()) {
            PetStatus status = existingStatus.get();
            status.setStatus(dto.getStatus());
            return petStatusRepository.save(status);
        } else {
            PetStatus newStatus = PetStatus.builder()
                    .pet(pet)
                    .user(user)
                    .status(dto.getStatus())
                    .build();
            return petStatusRepository.save(newStatus);
        }
    }

    public List<Pet> listAvailablePetsForUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Usuário com id " + userId + " não encontrado");
        }

        return petStatusRepository.findPetsNotInteractedByUser(userId);
    }

    public List<PetStatus> getPetsByUserAndStatus(Integer userId, PetStatusEnum status) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Usuário com id " + userId + " não encontrado");
        }

        return petStatusRepository.findByUserIdAndStatus(userId, status);
    }

    public void deletePetStatus(Integer petId, Integer userId) {
        PetStatus status = petStatusRepository.findByPetIdAndUserId(petId, userId)
                .orElseThrow(() -> new NotFoundException("Status não encontrado para pet " + petId + " e usuário " + userId));

        petStatusRepository.delete(status);
    }
}