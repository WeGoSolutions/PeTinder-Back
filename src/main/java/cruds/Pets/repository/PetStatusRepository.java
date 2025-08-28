package cruds.Pets.repository;

import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetStatusRepository extends JpaRepository<PetStatus, Integer> {

    List<PetStatus> findByUserIdAndStatus(UUID userId, PetStatusEnum status);

    Optional<PetStatus> findByPetIdAndUserId(Integer petId, UUID userId);

    @Query(value = "SELECT p FROM Pet p WHERE p.id NOT IN (SELECT ps.pet.id FROM PetStatus ps WHERE ps.user.id = :userId)")
    List<Pet> findPetsNotInteractedByUser(UUID userId);

    @Query(value = "SELECT ps FROM PetStatus ps WHERE ps.status = 'LIKED'")
    List<PetStatus> findAllLikedStatusPets();

    List<PetStatus> findLikedStatusPetsByUser_Id(UUID userId);

    Optional<PetStatus> findByPet_IdAndUser_Id(@NotNull Integer petId, @NotNull UUID userId);

    void deleteByPetIdAndUserIdNot(Integer petId, UUID userId);

    List<PetStatus> findByPet_Id(Integer petId);

    List<PetStatus> findByPet_IdAndStatus(Integer petId, PetStatusEnum status);

    @Modifying
    @Transactional
    void deleteByPetId(Integer id);

    @Modifying
    @Transactional
    void deleteByUserId(UUID id);
}