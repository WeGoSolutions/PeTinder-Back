package cruds.Ong.entity;

import cruds.Imagem.entity.ImagemForms;
import cruds.Imagem.entity.ImagemOng;
import cruds.Pets.entity.Pet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ong")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Ong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nome")
    private String nome;

    @Column(name = "razao_social")
    private String razaoSocial;

    @Column(name = "senha")
    private String senha;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fkImagemOng")
    private ImagemOng imagemOng;

    @OneToMany(mappedBy = "ong")
    private List<Pet> pets;

    @Column(name = "link")
    private String link;
}
