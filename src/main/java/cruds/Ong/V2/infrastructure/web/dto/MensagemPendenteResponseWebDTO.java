package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.adapter.MensagemPendenteGateway;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensagemPendenteResponseWebDTO {

    private UUID petId;
    private String petNome;
    private UUID userId;
    private String userName;
    private String userEmail;
    private LocalDateTime dataStatus;

    public static MensagemPendenteResponseWebDTO fromMensagem(
            MensagemPendenteGateway.MensagemPendente mensagem) {
        return MensagemPendenteResponseWebDTO.builder()
            .petId(mensagem.getPetId())
            .petNome(mensagem.getPetNome())
            .userId(mensagem.getUserId())
            .userName(mensagem.getUserName())
            .userEmail(mensagem.getUserEmail())
            .dataStatus(mensagem.getDataStatus())
            .build();
    }
}

