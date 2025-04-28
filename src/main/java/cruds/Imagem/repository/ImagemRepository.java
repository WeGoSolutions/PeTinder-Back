package cruds.Imagem.repository;

import cruds.Imagem.entity.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImagemRepository extends JpaRepository<Imagem, Integer> {
    List<Imagem> findByFormId(Integer formId);
}