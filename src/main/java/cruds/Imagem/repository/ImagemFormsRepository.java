package cruds.Imagem.repository;

import cruds.Imagem.entity.ImagemForms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagemFormsRepository extends JpaRepository<ImagemForms, Integer> {
    List<ImagemForms> findByFormId(Integer id);
}
