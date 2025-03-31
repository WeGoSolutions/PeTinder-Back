package cruds.Pets.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import cruds.Pets.Entities.Pet;

public interface PetRepository extends JpaRepository<Pet, Integer> {
}