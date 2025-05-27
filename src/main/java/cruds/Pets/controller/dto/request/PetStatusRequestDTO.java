package cruds.Pets.controller.dto.request;

import cruds.Pets.enums.PetStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PetStatusRequestDTO {
    @NotNull
    private Integer petId;

    @NotNull
    private Integer userId;

    @NotNull
    private PetStatusEnum status;

    private Integer curtidas;

    private LocalDateTime alteradoParaPending = LocalDateTime.now();
}
