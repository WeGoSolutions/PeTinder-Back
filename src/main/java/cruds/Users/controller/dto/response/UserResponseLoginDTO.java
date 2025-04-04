package cruds.Users.controller.dto.response;

import cruds.Users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseLoginDTO {

    private String nome;
    private String email;

    public static UserResponseLoginDTO toResponse(User user) {
        return UserResponseLoginDTO.builder()
                .nome(user.getNome())
                .email(user.getEmail())
                .build();
    }
}