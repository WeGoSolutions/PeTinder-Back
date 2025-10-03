package cruds.Dashboard.V2.infrastructure.persistence.jpa;

import cruds.Dashboard.V2.core.adapter.DashboardGateway;
import cruds.Dashboard.V2.core.domain.Dashboard;
import cruds.Dashboard.V2.infrastructure.persistence.jpa.mapper.DashboardMapper;
import cruds.Dashboard.repository.DashboardRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Primary
public class DashboardJpaAdapter implements DashboardGateway {

    private final DashboardRepository repository;

    public DashboardJpaAdapter(DashboardRepository repository) {
        this.repository = repository;
    }

    @Override
    public Dashboard salvar(Dashboard dashboard) {
        cruds.Dashboard.entity.Dashboard entity = DashboardMapper.toEntity(dashboard);
        cruds.Dashboard.entity.Dashboard savedEntity = repository.save(entity);
        return DashboardMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Dashboard> buscarPorId(UUID id) {
        return repository.findById(id)
                .map(DashboardMapper::toDomain);
    }

    @Override
    public Optional<Dashboard> buscarPorOngId(UUID ongId) {
        return repository.findByOngId(ongId)
                .map(DashboardMapper::toDomain);
    }

    @Override
    public void removerPorOngId(UUID ongId) {
        repository.deleteByOngId(ongId);
    }

    @Override
    public boolean existePorOngId(UUID ongId) {
        return repository.existsByOngId(ongId);
    }
}
