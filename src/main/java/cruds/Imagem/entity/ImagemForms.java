package cruds.Imagem.entity;

import cruds.Forms.entity.Forms;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "imagem_forms")
public class ImagemForms {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "idimagemForms")
    private UUID id;

    @Column(name = "link")
    private String caminho;

    @ManyToOne
    @JoinColumn(name = "fkForm")
    private Forms form;

    public ImagemForms(){}

    public ImagemForms(String caminho, Forms form) {
        this.caminho = caminho;
        this.form = form;
    }

    public ImagemForms(UUID id, String caminho, Forms form) {
        this.id = id;
        this.caminho = caminho;
        this.form = form;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public Forms getForm() {
        return form;
    }

    public void setForm(Forms form) {
        this.form = form;
    }
}
