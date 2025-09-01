package cruds.Pets.controller.dto.response;

import cruds.Pets.entity.Pet;
import cruds.Users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.core.util.internal.UnsafeUtil;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PetResponseUserPendenteDTO {
    private UUID idPet;
    private UUID idUser;
    private String nomeUser;
    private String imageUrl;

    public static PetResponseUserPendenteDTO toResponse(Pet pet, User user) {
        return PetResponseUserPendenteDTO.builder()
                .idPet(pet.getId())
                .idUser(user.getId())
                .nomeUser(user.getNome())
                .imageUrl(user.getImagemUser() != null ?
                        ("http://localhost:8080/users/" + user.getId() + "/imagens/0") : null)
                .build();
    }
}
