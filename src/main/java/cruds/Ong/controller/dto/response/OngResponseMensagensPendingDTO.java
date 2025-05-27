package cruds.Ong.controller.dto.response;

import cruds.Pets.entity.Pet;
import cruds.Users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class OngResponseMensagensPendingDTO {
    private Integer idOng;
    private Integer idUser;
    private Integer idPet;
    private String nomeUser;
    private String nomePet;
    private LocalDateTime dataHora;
    private String emailUser;
    private String imageUrl;

    public static OngResponseMensagensPendingDTO toResponse(Integer ongId, Pet pet, User user, LocalDateTime dataHora) {
        return OngResponseMensagensPendingDTO.builder()
                .idOng(ongId)
                .idUser(user.getId().intValue())
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
