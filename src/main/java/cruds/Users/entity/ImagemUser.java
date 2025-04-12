package cruds.Users.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_imagem_usuario")
public class ImagemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    private byte[] dados;

    public ImagemUser() {}

    public ImagemUser(byte[] dados) {
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
}