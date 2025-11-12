//package cruds.Pets.controller.dto.request;
//
//import cruds.Pets.enums.PetStatusEnum;
//import jakarta.validation.constraints.NotNull;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Data
//public class PetStatusRequestDTO {
//    @NotNull
//    private UUID petId;
//
//    @NotNull
//    private UUID userId;
//
//    @NotNull
//    private PetStatusEnum status;
//
//    private Integer curtidas;
//
//    private LocalDateTime alteradoParaPending = LocalDateTime.now();
//}
