package cruds.Dashboard.repository;

import cruds.Dashboard.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DashboardRepository extends JpaRepository<Dashboard, UUID> {
    void deleteByOngId(UUID id);
}
