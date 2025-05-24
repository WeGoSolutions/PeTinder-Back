package cruds.Imagem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import cruds.Pets.entity.Pet;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "imagem_pet")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idimagemPet")
    private Integer id;

    @Column(name = "link")
    private String caminho;

    @ManyToOne
    @JoinColumn(name = "fkPet")
    @JsonBackReference
    private Pet pet;

    public Imagem() {}

    public Imagem(String caminho) {
        this.caminho = caminho;
    }

    public Imagem(String caminho, Pet pet) {
        this.caminho = caminho;
        this.pet = pet;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}