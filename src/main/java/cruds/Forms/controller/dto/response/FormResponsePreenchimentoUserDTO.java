package cruds.Forms.controller.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class FormResponsePreenchimentoUserDTO {
    private String nome;
    private String cpf;
    private String email;
    private LocalDate dataNasc;
    private String cep;
    private String rua;
    private Integer numero;
    private String complemento;
    private String cidade;
    private String uf;
}