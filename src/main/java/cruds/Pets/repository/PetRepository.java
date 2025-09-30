package cruds.Pets.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import cruds.Pets.entity.Pet;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, UUID> {
    List<Pet> findByOngIdOrderByCurtidasDesc(UUID ongId);

    List<Pet> findByOngId(UUID ongId);

    void deleteByOngId(UUID id);

    @Query("SELECT p FROM Pet p ORDER BY p.id")
    List<Pet> findAllWithPagination(Pageable pageable);
}