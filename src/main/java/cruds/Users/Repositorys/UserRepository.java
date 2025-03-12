package cruds.Users.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import cruds.Users.Tables.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
