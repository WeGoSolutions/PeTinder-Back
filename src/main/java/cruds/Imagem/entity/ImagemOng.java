package cruds.Imagem.entity;

import cruds.Forms.entity.Forms;
import cruds.Imagem.repository.ImagemOngRepository;
import cruds.Ong.entity.Ong;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "imagemOng")
public class ImagemOng {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idImagemOng")
    private Integer id;

    @Column(name = "link")
    private String caminho;

    @OneToOne
    @JoinColumn(name = "fkOng")
    private Ong ong;

    public ImagemOng(String filePath, Ong ong) {
        this.caminho = filePath;
        this.ong = ong;
    }

    public ImagemOng() {

    }
}
