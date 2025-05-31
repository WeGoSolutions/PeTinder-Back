package cruds.Users.controller.dto.request;

import cruds.Users.entity.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoRequestDTO {
    @NotBlank
    private String cep;
    @NotBlank
    private String rua;
    @NotNull
    private Integer numero;
    @NotBlank
    private String cidade;
    @NotBlank
    private String uf;
    private String complemento;

    public Endereco toEntity() {
        return Endereco.builder()
                .cep(this.cep)
                .rua(this.rua)
                .numero(this.numero)
                .cidade(this.cidade)
                .uf(this.uf)
                .complemento(this.complemento)
                .build();
    }

    public Endereco toEntityWithId(Integer id) {
        return Endereco.builder()
                .id(id)
                .cep(this.cep)
                .rua(this.rua)
                .numero(this.numero)
                .cidade(this.cidade)
                .uf(this.uf)
                .complemento(this.complemento)
                .build();
    }

}