package cruds.Pets.controller.dto.response;

import cruds.Pets.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PetResponseCurtirDTO {
    private Integer id;
    private Boolean isLiked;
    private Integer curtidas;

    public static PetResponseCurtirDTO toResponse(Pet pet) {
        return PetResponseCurtirDTO.builder()
                .id(pet.getId())
                .curtidas(pet.getCurtidas())
                .build();
    }
}
