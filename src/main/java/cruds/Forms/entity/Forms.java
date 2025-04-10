package cruds.Forms.entity;

import cruds.Users.entity.User;
import jakarta.persistence.*;
import cruds.Pets.entity.Pet;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tb_forms")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Forms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    private String nome;
    @NotBlank
    private String cpf;
    @NotBlank
    private String email;

    @Temporal(TemporalType.DATE)
    @NotBlank
    private Date dataNasc;
    @NotBlank
    private String telefone;

    @NotBlank
    private String cep;

    private String complemento;

    @NotBlank
    private String rua;
    @NotBlank
    private Integer numero;
    @NotBlank
    private String cidade;
    @NotBlank
    private String uf;

    @NotBlank
    private String tipoMoradia;
    private String podeAnimal;
    private String infosCasa;
    private String possuiPet;
    private String infosPet;
}
