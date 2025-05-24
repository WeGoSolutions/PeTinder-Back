package cruds.Pets.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import cruds.Imagem.entity.Imagem;
import cruds.Ong.entity.Ong;
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
    @JsonManagedReference
    private List<Imagem> imagens;

    private Boolean isCastrado = false;

    private Boolean isVermifugo = false;

    private Boolean isVacinado = false;

    private Boolean isAdotado = false;

    @ManyToOne
    @JoinColumn(name = "ong_id", nullable = false)
    private Ong ong;

    private String status = "PENDING";
}