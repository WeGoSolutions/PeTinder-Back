package cruds.Ong.V2.infrastructure.persistence;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.domain.Ong;
import cruds.Ong.V2.infrastructure.persistence.jpa.OngEntity;
import cruds.Ong.V2.infrastructure.persistence.jpa.OngJpaRepository;
import cruds.Ong.V2.infrastructure.persistence.jpa.mapper.OngMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OngGatewayImpl implements OngGateway {

    private final OngJpaRepository ongJpaRepository;

    public OngGatewayImpl(OngJpaRepository ongJpaRepository) {
        this.ongJpaRepository = ongJpaRepository;
    }

    @Override
    public Ong salvar(Ong ong) {
        OngEntity entity = OngMapper.toEntity(ong);
        OngEntity savedEntity = ongJpaRepository.save(entity);
        return OngMapper.toDomain(savedEntity);
    }

    @Override
    public Ong atualizar(Ong ong) {
        OngEntity entity = OngMapper.toEntity(ong);
        OngEntity updatedEntity = ongJpaRepository.save(entity);
        return OngMapper.toDomain(updatedEntity);
    }

    @Override
    public Optional<Ong> buscarPorId(UUID id) {
        return ongJpaRepository.findById(id)
                .map(OngMapper::toDomain);
    }

    @Override
    public Optional<Ong> buscarPorEmail(String email) {
        return ongJpaRepository.findByEmail(email)
                .map(OngMapper::toDomain);
    }

    @Override
    public List<Ong> listarTodos() {
        return ongJpaRepository.findAll()
                .stream()
                .map(OngMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID id) {
        ongJpaRepository.deleteById(id);
    }

    @Override
    public boolean emailJaExiste(String email) {
        return ongJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existePorId(UUID id) {
        return ongJpaRepository.existsById(id);
    }
}
