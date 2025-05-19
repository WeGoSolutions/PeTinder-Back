package cruds.Imagem.entity;

import cruds.Forms.entity.Forms;
import jakarta.persistence.*;

@Entity
@Table(name = "imagem_forms")
public class ImagemForms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idimagemForms")
    private Integer id;

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

    public ImagemForms(Integer id, String caminho, Forms form) {
        this.id = id;
        this.caminho = caminho;
        this.form = form;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
