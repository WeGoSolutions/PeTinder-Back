package cruds.Pets.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetRequestCurtirDTO {
    private Boolean isLiked;

    public static PetRequestCurtirDTO toEntity(Boolean isLiked) {
        return PetRequestCurtirDTO.builder()
                .isLiked(isLiked)
                .build();
    }
}