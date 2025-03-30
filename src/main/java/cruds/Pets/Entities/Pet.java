package cruds.Pets.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tb_pet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true) //toBuilder altera no objeto instanciado, mas aqui nada é alterado
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private Double idade;
    private Double peso;
    private Double altura;
    private Integer curtidas;
    private List<String> tags;

    @ElementCollection
    @Lob
    private List<byte[]> imagem; // Armazena as imagens como bytes (BLOB)
}