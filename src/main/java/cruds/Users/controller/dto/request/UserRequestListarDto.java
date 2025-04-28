package cruds.Users.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserRequestListarDto {

    @Schema(description = "Id do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome do usuário", example = "Petinder")
    private String nome;

    @Schema(description = "Email do usuário", example = "petinder@gmail.com")
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
