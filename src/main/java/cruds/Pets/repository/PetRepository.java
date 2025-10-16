package cruds.Pets.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import cruds.Pets.entity.Pet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, UUID> {
    List<Pet> findByOngIdOrderByCurtidasDesc(UUID ongId);

    List<Pet> findByOngId(UUID ongId);

    void deleteByOngId(UUID id);

    Page<Pet> findAll(Pageable pageable);

    @Query("SELECT p FROM Pet p " +
            "LEFT JOIN FETCH p.tags " +
            "LEFT JOIN FETCH p.imagens " +
            "WHERE p.ong.id = :ongId")
    Page<Pet> findByOng_IdWithEverything(@Param("ongId") UUID ongId, Pageable pageable);

    Page<Pet> findByOng_Id(UUID ongId, Pageable pageable);

    // E se tiver relação com status também:
//    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.tags LEFT JOIN FETCH p.statusList WHERE p.ong.id = :ongId")
//    Page<Pet> findByOng_IdWithTagsAndStatus(@Param("ongId") UUID ongId, Pageable pageable);
}