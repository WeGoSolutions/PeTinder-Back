package sptech.school.crud_imagem.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.crud_imagem.Tables.Pet;

public interface PetRepository extends JpaRepository<Pet, Integer> {
}