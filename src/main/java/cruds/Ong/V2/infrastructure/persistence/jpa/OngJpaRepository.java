package cruds.Ong.V2.infrastructure.persistence.jpa;

import cruds.Ong.repository.OngRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OngJpaRepository extends JpaRepository<cruds.Ong.entity.Ong, UUID> {

    Optional<cruds.Ong.entity.Ong> findByEmail(String email);

    boolean existsByEmail(String email);
}

