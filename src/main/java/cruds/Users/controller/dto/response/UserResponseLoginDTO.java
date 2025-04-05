package cruds.Users.controller.dto.response;

import cruds.Users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseLoginDTO {
    private Integer id;
    private String email;
    private String senha;

    public static UserResponseLoginDTO toResponse(User user) {
        return UserResponseLoginDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .senha(user.getSenha())
                .build();
    }
}