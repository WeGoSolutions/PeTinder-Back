package cruds.Imagem.entity;

import cruds.Forms.entity.Forms;
import cruds.Ong.entity.Ong;
import jakarta.persistence.*;

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

}
