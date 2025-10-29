package cruds.Ong.V2.infrastructure.gateway;

import cruds.Ong.V2.core.adapter.MensagemPendenteGateway;
import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import cruds.Pets.repository.PetRepository;
import cruds.Pets.repository.PetStatusRepository;
import cruds.Users.V2.core.domain.Usuario;
import cruds.Users.V2.infrastructure.persistence.jpa.UsuarioEntity;
import cruds.Users.V2.infrastructure.persistence.jpa.UsuarioJpaRepository;
import cruds.Users.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MensagemPendenteGatewayImpl implements MensagemPendenteGateway {

    private final PetRepository petRepository;
    private final PetStatusRepository petStatusRepository;
    private final UsuarioJpaRepository usuarioRepository;

    public MensagemPendenteGatewayImpl(PetRepository petRepository,
                                       PetStatusRepository petStatusRepository,
                                       UsuarioJpaRepository usuarioRepository) {
        this.petRepository = petRepository;
        this.petStatusRepository = petStatusRepository;
        this.usuarioRepository = usuarioRepository;
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
                String email = user.getEmail();
                Optional<UsuarioEntity> usuario = usuarioRepository.findByEmail(email);
                mensagensPendentes.add(new MensagemPendente(
                    pet.getId(),
                    pet.getNome(),
                    user.getId(),
                    user.getNome(),
                    user.getEmail(),
                    usuario.get().getImagemUser().getDados(),
                    status.getAlteradoParaPending()
                ));
            }
        }

        return mensagensPendentes;
    }
}

