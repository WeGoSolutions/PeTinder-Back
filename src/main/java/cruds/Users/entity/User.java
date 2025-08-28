package cruds.Users.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Temporal(TemporalType.DATE)
    private LocalDate dataNasc;

    @Column(unique = true)
    private String cpf;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_endereco")
    private Endereco endereco;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_imagem_usuario")
    private ImagemUser imagemUser;

    private Boolean userNovo;
}