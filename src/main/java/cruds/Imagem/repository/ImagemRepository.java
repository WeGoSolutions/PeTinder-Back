package cruds.Imagem.repository;

import cruds.Imagem.entity.Imagem;
import cruds.Imagem.entity.ImagemForms;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ImagemRepository extends JpaRepository<Imagem, UUID> {
}