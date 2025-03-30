package cruds.Pets.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDTO {
    private String nome;
    private Double idade;
    private Double peso;
    private Double altura;
    private Integer curtidas;
    private List<String> tags;
    private List<String> imagemBase64;
}