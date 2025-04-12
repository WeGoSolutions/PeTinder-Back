package cruds.Imagem.entity;

import cruds.Forms.entity.Forms;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_imagem")
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    private byte[] dados;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Forms form;

    public Imagem() {}

    public Imagem(byte[] dados, Forms form) {
        this.dados = dados;
        this.form = form;
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

    public Forms getForm() {
        return form;
    }

    public void setForm(Forms form) {
        this.form = form;
    }
}