package cruds.Imagem.entity;

import cruds.Forms.entity.Forms;
import cruds.Imagem.repository.ImagemOngRepository;
import cruds.Ong.entity.Ong;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "imagem_ong")
@Getter
public class ImagemOng {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "link")
    private String caminho;

    @OneToOne(mappedBy = "imagemOng")
    private Ong ong;

    public ImagemOng(String filePath, Ong ong) {
        this.caminho = filePath;
        this.ong = ong;
    }

    public ImagemOng() {

    }

    public String getArquivo() {
        return caminho;
    }
}
