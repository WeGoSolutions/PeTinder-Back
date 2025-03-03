package sptech.school.crud_imagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetRepository repository;

    @PostMapping
    public ResponseEntity<Pet> cadastrarPet(@RequestBody PetDTO dto) {

        // Validações simples
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (dto.getIdade() == null || dto.getIdade() < 0) {
            return ResponseEntity.badRequest().build();
        }
        if (dto.getPeso() == null || dto.getPeso() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (dto.getAltura() == null || dto.getAltura() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        // Monta a entidade Pet
        Pet petNovo = new Pet();
        petNovo.setNome(dto.getNome());
        petNovo.setIdade(dto.getIdade());
        petNovo.setPeso(dto.getPeso());
        petNovo.setAltura(dto.getAltura());

        // Decodifica a imagem Base64 se existir
        if (dto.getImagemBase64() != null && !dto.getImagemBase64().isEmpty()) {
            try {
                byte[] imagemBytes = Base64.getDecoder().decode(dto.getImagemBase64());
                petNovo.setImagem(imagemBytes);
            } catch (IllegalArgumentException e) {
                // Caso a string Base64 seja inválida
                return ResponseEntity.badRequest().build();
            }
        }

        // Salva no banco
        Pet petCadastrado = repository.save(petNovo);

        // Retorna o Pet cadastrado
        return ResponseEntity.status(HttpStatus.CREATED).body(petCadastrado);
    }

    // Endpoint para retornar a imagem em binário
    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> getImagem(@PathVariable Integer id) {
        Pet pet = repository.findById(id).orElse(null);
        if (pet == null || pet.getImagem() == null) {
            return ResponseEntity.notFound().build();
        }

        // Ajuste o MediaType conforme o tipo real da imagem (PNG, JPEG, etc.)
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(pet.getImagem());
    }
}
