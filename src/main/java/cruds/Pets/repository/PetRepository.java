package cruds.Pets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cruds.Pets.entity.Pet;

import java.util.List;
import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, UUID> {
    List<Pet> findByOngIdOrderByCurtidasDesc(UUID ongId);

    List<Pet> findByOngId(UUID ongId);

    void deleteByOngId(UUID id);
}