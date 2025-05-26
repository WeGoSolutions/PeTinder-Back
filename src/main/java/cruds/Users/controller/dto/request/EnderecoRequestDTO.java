package cruds.Users.controller.dto.request;

import cruds.Users.entity.Endereco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoRequestDTO {
    private String cep;
    private String rua;
    private Integer numero;
    private String cidade;
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
}