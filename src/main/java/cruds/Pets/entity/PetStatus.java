package cruds.Pets.entity;

import cruds.Pets.enums.PetStatusEnum;
import cruds.Users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet_status")
public class PetStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_pet")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "fk_usuario")
    private User user;

    @Enumerated(EnumType.STRING)
    private PetStatusEnum status;

    private LocalDateTime alteradoParaPending;
}
