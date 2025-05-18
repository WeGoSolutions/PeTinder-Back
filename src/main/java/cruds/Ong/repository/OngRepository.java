package cruds.Ong.repository;

import cruds.Ong.entity.Ong;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OngRepository extends JpaRepository<Ong, Integer> {
    Optional<Ong> findByEmail(@NotBlank @Email String email);

    Optional<Ong> findByEmailandSenha(@Email @NotBlank String email, @NotBlank String senha);
}
