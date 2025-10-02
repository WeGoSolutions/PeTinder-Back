package cruds.Pets.V2.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetJpaRepository extends JpaRepository<PetEntity, UUID> {

    List<PetEntity> findByOngIdOrderByCurtidasDesc(UUID ongId);

    List<PetEntity> findByOngId(UUID ongId);

    @Query("SELECT p FROM PetEntity p WHERE p.isAdotado = false OR p.isAdotado IS NULL")
    List<PetEntity> findByIsAdotadoFalseOrIsAdotadoIsNull();

    void deleteByOngId(UUID ongId);
}