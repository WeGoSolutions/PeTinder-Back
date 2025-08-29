package cruds.Users.V2.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade JPA para persistência de imagem - Infrastructure Layer
 */
@Entity
@Table(name = "imagem_usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ImagemUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    private byte[] dados;

    private String arquivo;
}
