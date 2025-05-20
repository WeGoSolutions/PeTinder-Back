package cruds.Dashboard.entity;

import cruds.Ong.entity.Ong;
import jakarta.persistence.*;

@Entity
public class Dashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Ong ong;
}
