package cruds.Dashboard.V2.infrastructure.persistence.jpa.mapper;

import cruds.Dashboard.V2.core.domain.PetDashboard;
import cruds.Pets.entity.Pet;

public class PetDashboardMapper {

    public static PetDashboard toDomain(Pet entity) {
        if (entity == null) return null;

        return new PetDashboard(
            entity.getId(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getIdade(),
            entity.getPorte(),
            entity.getSexo(),
            entity.getIsCastrado(),
            entity.getIsVermifugo(),
            entity.getIsVacinado(),
            entity.getIsAdopted(),
            entity.getCurtidas()
        );
    }
}
