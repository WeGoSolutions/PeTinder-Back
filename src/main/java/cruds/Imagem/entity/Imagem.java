package cruds.Imagem.entity;

import cruds.Forms.entity.Forms;
import cruds.Pets.entity.Pet;
import jakarta.persistence.*;

@Entity
@Table(name = "imagem_pet")
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idimagemPet")
    private Integer id;


    @Column(name = "link")
    private String caminho;

    @ManyToOne
    @JoinColumn(name = "fkPet")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private Forms form;

    public Imagem() {}

    public Imagem(String caminho) {
        this.caminho = caminho;
    }

    public Imagem(String caminho, Forms form) {
        this.caminho = caminho;
        this.form = form;
    }

    public Imagem(String caminho, Pet pet) {
        this.caminho = caminho;
        this.pet = pet;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
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