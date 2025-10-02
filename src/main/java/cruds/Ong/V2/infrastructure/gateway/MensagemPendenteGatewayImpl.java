package cruds.Ong.V2.infrastructure.gateway;

import cruds.Ong.V2.core.adapter.MensagemPendenteGateway;
import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import cruds.Pets.repository.PetRepository;
import cruds.Pets.repository.PetStatusRepository;
import cruds.Users.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MensagemPendenteGatewayImpl implements MensagemPendenteGateway {

    private final PetRepository petRepository;
    private final PetStatusRepository petStatusRepository;

    public MensagemPendenteGatewayImpl(PetRepository petRepository,
                                       PetStatusRepository petStatusRepository) {
        this.petRepository = petRepository;
        this.petStatusRepository = petStatusRepository;
    }

    @Override
    public List<MensagemPendente> listarMensagensPendentes(UUID ongId) {
        List<Pet> petsOng = petRepository.findByOngId(ongId);
        List<MensagemPendente> mensagensPendentes = new ArrayList<>();

        for (Pet pet : petsOng) {
            List<PetStatus> statusList = petStatusRepository.findByPet_IdAndStatus(
                pet.getId(),
                PetStatusEnum.PENDING
            );

            for (PetStatus status : statusList) {
                User user = status.getUser();
                mensagensPendentes.add(new MensagemPendente(
                    pet.getId(),
                    pet.getNome(),
                    user.getId(),
                    user.getNome(),
                    user.getEmail(),
                    status.getAlteradoParaPending()
                ));
            }
        }

        return mensagensPendentes;
    }
}

