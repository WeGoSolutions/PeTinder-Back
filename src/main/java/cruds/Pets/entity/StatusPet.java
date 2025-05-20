package cruds.Pets.entity;

import cruds.Users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusPet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Pet pet;

    @ManyToOne
    private User usuario;

    @Enumerated(EnumType.STRING)
    private Status status;
}
