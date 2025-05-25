package cruds.Users.controller.dto.response;

import cruds.Users.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseUrlDTO {
    private Long id;
    private String nome;
    private String email;
    private String imageUrl;

    public static UserResponseUrlDTO toResponse(User user) {
        return UserResponseUrlDTO.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .imageUrl(user.getImagemUser() != null ?
                        ("http://localhost:8080/users/" + user.getId() + "/imagens/0") : null)
                .build();
    }
}