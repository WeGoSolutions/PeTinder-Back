package cruds.Users.V2.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade JPA para persistência de endereço - Infrastructure Layer
 */
@Entity
@Table(name = "endereco")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EnderecoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cep;
    private String rua;
    private Integer numero;
    private String cidade;
    private String uf;
    private String complemento;
}
