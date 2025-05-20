package cruds.Dashboard.repository;

import cruds.Dashboard.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardRepository extends JpaRepository<Dashboard, Integer> {
}
