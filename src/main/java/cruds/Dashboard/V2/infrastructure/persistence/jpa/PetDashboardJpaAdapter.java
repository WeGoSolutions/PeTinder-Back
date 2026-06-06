package cruds.Dashboard.V2.infrastructure.persistence.jpa;

import cruds.Dashboard.V2.core.adapter.PetDashboardGateway;
import cruds.Dashboard.V2.core.domain.PetDashboard;
import cruds.Dashboard.V2.infrastructure.persistence.jpa.mapper.PetDashboardMapper;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetStatusJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PetDashboardJpaAdapter implements PetDashboardGateway {

    private final PetJpaRepository petRepository;
    private final PetStatusJpaRepository petStatusRepository;

    public PetDashboardJpaAdapter(PetJpaRepository petRepository,
                                  PetStatusJpaRepository petStatusRepository) {
        this.petRepository = petRepository;
        this.petStatusRepository = petStatusRepository;
    }

    @Override
    public List<PetDashboard> listarPetsPorOngIdOrderByCurtidas(UUID ongId) {
        // A coluna `curtidas` do pet NAO reflete os likes reais (eles vivem em
        // pet_status). Contamos os LIKED de verdade por pet e reordenamos por eles,
        // senao o grafico "Pets mais curtidos" mostra todo mundo com 0.
        return petRepository.findByOngIdOrderByCurtidasDesc(ongId).stream()
                .map(entity -> {
                    PetDashboard dash = PetDashboardMapper.toDomain(entity);
                    long likes = petStatusRepository.countByPetIdAndStatus(entity.getId(), PetStatusEnum.LIKED);
                    dash.setCurtidas(Math.toIntExact(likes));
                    return dash;
                })
                .sorted(Comparator.comparingInt(
                        (PetDashboard p) -> p.getCurtidas() == null ? 0 : p.getCurtidas()).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<PetDashboard> listarPetsPorOngId(UUID ongId) {
        return petRepository.findByOngId(ongId).stream()
                .map(PetDashboardMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long contarPetsAdotadosPorOngId(UUID ongId) {
        return petRepository.findByOngId(ongId).stream()
                .filter(pet -> Boolean.TRUE.equals(pet.getIsAdotado()))
                .count();
    }

    @Override
    public long contarPetsNaoAdotadosPorOngId(UUID ongId) {
        return petRepository.findByOngId(ongId).stream()
                .filter(pet -> !Boolean.TRUE.equals(pet.getIsAdotado()))
                .count();
    }
}

