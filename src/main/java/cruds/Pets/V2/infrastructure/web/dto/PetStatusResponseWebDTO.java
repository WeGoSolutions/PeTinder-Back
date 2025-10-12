package cruds.Pets.V2.infrastructure.web.dto;

import cruds.Pets.entity.PetStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetStatusResponseWebDTO {

    private UUID petId;
    private String petNome;
    private UUID usuarioId;
    private String status;
    private String imageUrl;

    public static PetStatusResponseWebDTO fromEntity(PetStatus petStatus) {
        PetStatusResponseWebDTO dto = new PetStatusResponseWebDTO();
        dto.petId = petStatus.getPet().getId();
        dto.petNome = petStatus.getPet().getNome();
        dto.usuarioId = petStatus.getUser().getId();
        dto.status = petStatus.getStatus().name();

        var pet = petStatus.getPet();
        if (pet.getImagens() != null && !pet.getImagens().isEmpty()) {
            String base = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .build()
                    .toUriString();
            dto.imageUrl = base + "/pets/" + pet.getId() + "/imagens/0";
        }

        return dto;
    }
}
