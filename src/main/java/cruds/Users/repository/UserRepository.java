package cruds.Users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cruds.Users.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByEmail(String email);

    List<User> findByCpf(String cpf);
}
