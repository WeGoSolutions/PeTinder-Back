package cruds.Pets.repository;

import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PetStatusRepository extends JpaRepository<PetStatus, Integer> {

    List<PetStatus> findByUserId(Integer userId);

    List<PetStatus> findByUserIdAndStatus(Integer userId, PetStatusEnum status);

    Optional<PetStatus> findByPetIdAndUserId(Integer petId, Integer userId);

    @Query("SELECT p FROM Pet p WHERE p.id NOT IN (SELECT ps.pet.id FROM PetStatus ps WHERE ps.user.id = :userId)")
    List<Pet> findPetsNotInteractedByUser(Integer userId);

    List<PetStatus> findAllLikedStatusPets();

    List<PetStatus> findLikedStatusPetsByUsuarioId(Integer usuarioId);
}
