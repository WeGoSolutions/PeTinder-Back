package cruds.Forms.controller.dto.response;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class FormResponseCriarDTO {
    private Integer id;
    private Integer petId;
    private Integer userId;

    private String nome;
    private String cpf;
    private String email;
    private Date dataNasc;
    private String telefone;
    private String cep;
    private String complemento;
    private String rua;
    private Integer numero;
    private String cidade;
    private String uf;
    private String tipoMoradia;
    private String aluguePodeAnimal;
    private String infosCasa;
    private String possuiPet;
    private String castradoOrVacinado;
    private String infosPet;
    private List<byte[]> imagens;

    private boolean finalizado;
}