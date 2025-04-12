package cruds.Forms.repository;

import cruds.Forms.entity.Forms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormsRepository extends JpaRepository<Forms, Integer> {
}
