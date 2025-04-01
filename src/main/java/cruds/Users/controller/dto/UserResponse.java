package cruds.Users.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String nome;
    private String email;

    public static UserResponse toResponse(UserRequest user) {
        return UserResponse.builder()
                .nome(user.getNome())
                .email(user.getEmail())
                .build();
    }

}
