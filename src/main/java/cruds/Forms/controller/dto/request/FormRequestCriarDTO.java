package cruds.Forms.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class FormRequestCriarDTO {
    @NotNull
    private Integer petId;
    @NotNull
    private Integer userId;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    private String email;

    @NotNull
    private Date dataNasc;

    @NotBlank
    private String telefone;

    @NotBlank
    private String cep;

    private String complemento;

    @NotBlank
    private String rua;

    @NotNull
    private Integer numero;

    @NotBlank
    private String cidade;

    @NotBlank
    private String uf;

    @NotBlank
    private String tipoMoradia;

    private String aluguePodeAnimal;

    private String infosCasa;

    @NotBlank
    private String possuiPet;

    private String castradoOrVacinado;

    private String infosPet;

    @NotNull
    @Size(min = 5, message = "Deve haver no mínimo 5 imagens")
    private List<byte[]> imagens;
}