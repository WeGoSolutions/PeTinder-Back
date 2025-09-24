package cruds.Pets.V2.infrastructure.persistence.jpa;

import cruds.Pets.V2.core.domain.PetStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PetStatusJpaRepository extends JpaRepository<PetStatusEntity, UUID> {
    
    Optional<PetStatusEntity> findByPetIdAndUserId(UUID petId, UUID userId);
    
    List<PetStatusEntity> findByUserId(UUID userId);
    
    List<PetStatusEntity> findByPetId(UUID petId);
    
    List<PetStatusEntity> findByUserIdAndStatus(UUID userId, PetStatusEnum status);
    
    List<PetStatusEntity> findByStatus(PetStatusEnum status);
    
    void deleteByPetIdAndUserId(UUID petId, UUID userId);
    
    void deleteByPetId(UUID petId);
    
    boolean existsByPetIdAndUserId(UUID petId, UUID userId);
    
    @Query("SELECT DISTINCT p.id FROM Pet p WHERE p.id NOT IN " +
           "(SELECT ps.petId FROM PetStatusEntity ps WHERE ps.userId = :userId)")
    List<UUID> findPetsNotInteractedByUser(@Param("userId") UUID userId);
}