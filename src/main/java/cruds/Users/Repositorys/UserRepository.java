package cruds.Users.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import cruds.Users.Tables.User;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByEmail(String email);

    List<User> findByCpf(String cpf);
}
