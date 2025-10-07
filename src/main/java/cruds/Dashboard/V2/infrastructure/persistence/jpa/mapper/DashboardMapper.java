package cruds.Dashboard.V2.infrastructure.persistence.jpa.mapper;

import cruds.Dashboard.V2.core.domain.Dashboard;
import cruds.Ong.entity.Ong;

import java.util.UUID;

public class DashboardMapper {

    public static cruds.Dashboard.entity.Dashboard toEntity(Dashboard domain) {
        if (domain == null) return null;

        Ong ong = new Ong();
        ong.setId(domain.getOngId());

        return cruds.Dashboard.entity.Dashboard.builder()
                .id(domain.getId())
                .ong(ong)
                .build();
    }

    public static Dashboard toDomain(cruds.Dashboard.entity.Dashboard entity) {
        if (entity == null) return null;

        UUID ongId = entity.getOng() != null ? entity.getOng().getId() : null;

        Dashboard dashboard = new Dashboard(entity.getId(), ongId);
        return dashboard;
    }
}
