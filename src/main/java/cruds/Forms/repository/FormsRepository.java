package cruds.Forms.repository;

import cruds.Forms.entity.Forms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FormsRepository extends JpaRepository<Forms, UUID> {
}
