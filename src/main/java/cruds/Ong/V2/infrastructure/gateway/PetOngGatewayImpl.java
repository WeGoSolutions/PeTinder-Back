package cruds.Ong.V2.infrastructure.gateway;

import cruds.Ong.V2.core.adapter.PetOngGateway;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetStatusJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PetOngGatewayImpl implements PetOngGateway {

    private final PetJpaRepository petRepository;
    private final PetStatusJpaRepository petStatusRepository;

    public PetOngGatewayImpl(PetJpaRepository petRepository, PetStatusJpaRepository petStatusRepository) {
        this.petRepository = petRepository;
        this.petStatusRepository = petStatusRepository;
    }

    @Override
    public Page<PetOngInfo> listarPetsPorOng(UUID ongId, Pageable pageable) {
        Page<PetEntity> petsPage = petRepository.findByOngId(ongId, pageable);

        return petsPage.map(pet -> {
            List<String> statusList = petStatusRepository.findByPetId(pet.getId())
                    .stream()
                    .map(status -> status.getStatus().name())
                    .collect(Collectors.toList());

            // Gerar URLs das imagens
            String baseUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .build()
                    .toUriString();

            // Imagens não estão disponíveis diretamente no PetEntity, então retornamos null por enquanto
            List<String> imageUrls = null;

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
        });
    }

    @Override
    public void removerPetsPorOng(UUID ongId) {
        petRepository.deleteByOngId(ongId);
    }
}