package cruds.Pets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cruds.Pets.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Integer> {
}