package cruds.Ong.V2.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OngJpaRepository extends JpaRepository<OngEntity, UUID> {
    
    Optional<OngEntity> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
