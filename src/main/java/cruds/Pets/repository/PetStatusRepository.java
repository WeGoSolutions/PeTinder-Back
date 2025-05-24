package cruds.Pets.repository;

import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PetStatusRepository extends JpaRepository<PetStatus, Integer> {

    List<PetStatus> findByUserId(Integer userId);

    List<PetStatus> findByUserIdAndStatus(Integer userId, PetStatusEnum status);

    Optional<PetStatus> findByPetIdAndUserId(Integer petId, Integer userId);

    @Query(value = "SELECT p FROM Pet p WHERE p.id NOT IN (SELECT ps.pet.id FROM PetStatus ps WHERE ps.user.id = :userId)")
    List<Pet> findPetsNotInteractedByUser(Integer userId);

    @Query(value = "SELECT ps FROM PetStatus ps WHERE ps.status = 'LIKED'")
    List<PetStatus> findAllLikedStatusPets();

    List<PetStatus> findLikedStatusPetsByUser_Id(Integer userId);

    Optional<PetStatus> findByPet_IdAndUser_Id(@NotNull Integer petId, @NotNull Integer userId);
}