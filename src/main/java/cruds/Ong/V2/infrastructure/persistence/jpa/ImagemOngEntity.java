package cruds.Ong.V2.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "imagem_ong")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImagemOngEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Lob
    @Column(name = "dados", columnDefinition = "LONGBLOB")
    private byte[] dados;

    @Column(name = "arquivo")
    private String arquivo;
}
