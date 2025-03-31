package cruds.Users.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserResponse {

    private String nome;
    private String email;

    public UserResponse() {}

    public UserResponse(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public @NotBlank String getNome() {
        return nome;
    }

    public void setNome(@NotBlank String nome) {
        this.nome = nome;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public static UserResponse toResponse(UserRequest user) {
        UserResponse dto = new UserResponse();
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        return dto;
    }

}
