package cruds.Imagem.entity;

import cruds.Forms.entity.Forms;
import cruds.Imagem.repository.ImagemOngRepository;
import cruds.Ong.entity.Ong;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Entity
@Table(name = "imagem_ong")
@Getter
public class ImagemOng {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Lob
    private byte[] dados;

    private String arquivo;

    public ImagemOng() {}

    public ImagemOng(byte[] dados) {
        this.dados = dados;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
