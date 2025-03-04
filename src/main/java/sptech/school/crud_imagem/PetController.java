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

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            return ResponseEntity.status(400).build();
        }
        if (dto.getIdade() == null || dto.getIdade() < 0) {
            return ResponseEntity.status(400).build();
        }
        if (dto.getPeso() == null || dto.getPeso() <= 0) {
            return ResponseEntity.status(400).build();
        }
        if (dto.getAltura() == null || dto.getAltura() <= 0) {
            return ResponseEntity.status(400).build();
        }

        Pet petNovo = new Pet();
        petNovo.setNome(dto.getNome());
        petNovo.setIdade(dto.getIdade());
        petNovo.setPeso(dto.getPeso());
        petNovo.setAltura(dto.getAltura());

        if (dto.getImagemBase64() != null && !dto.getImagemBase64().isEmpty()) {
            try {
                byte[] imagemBytes = Base64.getDecoder().decode(dto.getImagemBase64());
                petNovo.setImagem(imagemBytes);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        Pet petCadastrado = repository.save(petNovo);

        return ResponseEntity.status(201).body(petCadastrado);
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> getImagem(@PathVariable Integer id) {
        Pet pet = repository.findById(id).orElse(null);
        if (pet == null || pet.getImagem() == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) // QUEM TA DANDO APROVE, TEM QUE VALIDAR SE TODAS AS IMAGENS VAO SER .PNG
                .body(pet.getImagem());
    }
}
