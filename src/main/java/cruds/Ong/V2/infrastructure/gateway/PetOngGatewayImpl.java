package cruds.Ong.V2.infrastructure.gateway;

import cruds.Ong.V2.core.adapter.PetOngGateway;
import cruds.Pets.entity.Pet;
import cruds.Pets.repository.PetRepository;
import cruds.Pets.repository.PetStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PetOngGatewayImpl implements PetOngGateway {

    private final PetRepository petRepository;
    private final PetStatusRepository petStatusRepository;

    public PetOngGatewayImpl(PetRepository petRepository, PetStatusRepository petStatusRepository) {
        this.petRepository = petRepository;
        this.petStatusRepository = petStatusRepository;
    }

    @Override
    public List<PetOngInfo> listarPetsPorOng(UUID ongId) {
        List<Pet> pets = petRepository.findByOngId(ongId);

        return pets.stream().map(pet -> {
            List<String> statusList = petStatusRepository.findByPet_Id(pet.getId())
                    .stream()
                    .map(status -> status.getStatus().name())
                    .collect(Collectors.toList());

            // Converter idade de Double para Integer
            Integer idade = pet.getIdade() != null ? pet.getIdade().intValue() : null;

            return new PetOngInfo(
                pet.getId(),
                pet.getNome(),
                null, // Pet não tem campo raca
                pet.getPorte(),
                idade,
                pet.getSexo(),
                pet.getDescricao(),
                pet.getIsAdopted(), // Campo correto é isAdopted
                statusList
            );
        }).collect(Collectors.toList());
    }

    @Override
    public void removerPetsPorOng(UUID ongId) {
        petRepository.deleteByOngId(ongId);
    }
}
