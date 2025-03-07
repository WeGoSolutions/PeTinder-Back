package sptech.school.crud_imagem.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.crud_imagem.Tables.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
