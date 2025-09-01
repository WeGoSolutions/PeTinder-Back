package cruds.Dashboard.entity;

import cruds.Ong.entity.Ong;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "dashboard")
public class Dashboard {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @JoinColumn(name = "fkOng")
    @OneToOne()
    private Ong ong;
}
