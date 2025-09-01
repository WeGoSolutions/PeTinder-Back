package cruds.Ong.controller.dto.response;

import cruds.Pets.entity.Pet;
import cruds.Users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class OngResponseMensagensPendingDTO {
    private UUID idOng;
    private UUID idUser;
    private UUID idPet;
    private String nomeUser;
    private String nomePet;
    private LocalDateTime dataHora;
    private String emailUser;
    private String imageUrl;

    public static OngResponseMensagensPendingDTO toResponse(UUID ongId, Pet pet, User user, LocalDateTime dataHora) {
        return OngResponseMensagensPendingDTO.builder()
                .idOng(ongId)
                .idUser(user.getId())
                .idPet(pet.getId())
                .nomeUser(user.getNome())
                .nomePet(pet.getNome())
                .dataHora(dataHora)
                .emailUser(user.getEmail())
                .imageUrl(user.getImagemUser() != null ?
                        ("http://localhost:8080/users/" + user.getId() + "/imagens/0") : null)
                .build();
    }
}
