package cruds.Pets.service;

import cruds.Pets.controller.dto.request.PetStatusRequestDTO;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import cruds.Pets.repository.PetRepository;
import cruds.Pets.repository.PetStatusRepository;
import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public List<PetResponseGeralDTO> listAvailablePetsForUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Usuário com id " + userId + " não encontrado");
        }
        return petStatusRepository.findPetsNotInteractedByUser(userId)
                .stream()
                .filter((Pet pet) -> pet.getIsAdopted() == null || !pet.getIsAdopted())
                .map(PetResponseGeralDTO::toResponse)
                .collect(Collectors.toList());
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

    public List<PetResponseGeralDTO> listDefaultPets(Integer userId) {
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

    public void incrementarCurtidasPet(Integer petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet não encontrado"));
        int atual = pet.getCurtidas() != null ? pet.getCurtidas() : 0;
        pet.setCurtidas(atual + 1);
        petRepository.save(pet);
    }

    public void publicarAdocao(Integer petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet não encontrado"));
        pet.setIsAdopted(true);
        petRepository.save(pet);
    }
}