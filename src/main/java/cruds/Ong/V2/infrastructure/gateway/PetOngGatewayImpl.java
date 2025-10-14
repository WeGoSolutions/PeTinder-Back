package cruds.Ong.V2.infrastructure.gateway;

import cruds.Ong.V2.core.adapter.PetOngGateway;
import cruds.Pets.entity.Pet;
import cruds.Pets.repository.PetRepository;
import cruds.Pets.repository.PetStatusRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

            // Gerar URLs das imagens
            String baseUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .build()
                    .toUriString();

            List<String> imageUrls = pet.getImagens() == null
                    ? null
                    : IntStream.range(0, pet.getImagens().size())
                    .mapToObj(i -> baseUri + "/pets/" + pet.getId() + "/imagens/" + i)
                    .collect(Collectors.toList());

            return new PetOngInfo(
                ongId,
                pet.getId(),
                pet.getNome(),
                pet.getIdade(),
                pet.getPorte(),
                pet.getCurtidas(),
                pet.getTags(),
                pet.getDescricao(),
                pet.getIsCastrado(),
                pet.getIsVermifugo(),
                pet.getIsVacinado(),
                imageUrls,
                pet.getSexo(),
                statusList
            );
        }).collect(Collectors.toList());
    }

    @Override
    public void removerPetsPorOng(UUID ongId) {
        petRepository.deleteByOngId(ongId);
    }
}
