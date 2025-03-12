package cruds.Pets.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import cruds.Pets.Tables.Pet;

public interface PetRepository extends JpaRepository<Pet, Integer> {
}