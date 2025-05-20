package cruds.Pets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cruds.Pets.entity.Pet;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    List<Pet> findByOngIdOrderByCurtidasDesc(Integer ongId);

    List<Pet> findByOngId(Integer ongId);
}