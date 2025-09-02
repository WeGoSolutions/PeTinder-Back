package cruds.Users.V2.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

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
    private EnderecoEntity endereco;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_imagem_usuario")
    private ImagemUsuarioEntity imagemUser;

    private Boolean userNovo;
}
