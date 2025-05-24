package cruds.Imagem.repository;

import cruds.Imagem.entity.ImagemOng;
import cruds.Ong.entity.Ong;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagemOngRepository extends JpaRepository<ImagemOng, Integer> {
}
