package cruds.Pets.entity;

import cruds.Imagem.entity.Imagem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "pet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private Double idade;

    private Double peso;

    private Double altura;

    private Integer curtidas;

    @ElementCollection
    private List<String> tags;

    private String descricao;

    private Boolean isLiked = false;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagem> imagens;

}