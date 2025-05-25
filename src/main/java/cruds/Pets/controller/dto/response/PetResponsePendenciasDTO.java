package cruds.Pets.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetResponsePendenciasDTO {
    private String nome;
    private List<String> pendencias;
    private String imagemPet;

    public PetResponsePendenciasDTO(String nome, List<String> pendencias) {
        this.nome = nome;
        this.pendencias = pendencias;
    }

    public PetResponsePendenciasDTO(String nome, List<String> pendencias, Integer petId) {
        this.nome = nome;
        this.pendencias = pendencias;
        this.imagemPet = "http://localhost:8080/pets/" + petId + "/imagens/0";
    }
}
