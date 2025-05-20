package cruds.Pets.repository;

import cruds.Pets.entity.StatusPet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatusPetRepository extends JpaRepository<StatusPet, Integer> {

    @Query("SELECT s FROM StatusPet s WHERE s.status = 'LIKED'")
    List<StatusPet> findAllLikedStatusPets();

    @Query("SELECT s FROM StatusPet s WHERE s.status = 'LIKED' AND s.usuario.id = :usuarioId")
    List<StatusPet> findLikedStatusPetsByUsuarioId(Integer usuarioId);

}
