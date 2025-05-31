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
    private Integer id;

    @Lob
    private byte[] dados;

    private String arquivo;

    public ImagemOng() {}

    public ImagemOng(byte[] dados) {
        this.dados = dados;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getDados() {
        return dados;
    }

    public void setDados(byte[] dados) {
        this.dados = dados;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getArquivo() {
        return arquivo;
    }
}
