package cruds.Pets.V2.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImagemPetJpaRepository extends JpaRepository<ImagemPetEntity, UUID> {
    
    List<ImagemPetEntity> findByPetIdOrderById(UUID petId);
    
    void deleteByPetId(UUID petId);
}