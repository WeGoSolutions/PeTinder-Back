package sptech.school.crud_imagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@RestController
@RequestMapping("/pet")
public class PetController  {

    @Autowired
    private PetRepository repository;

    @PostMapping
    public ResponseEntity<Pet> cadastrarPet(@RequestBody Pet pet) {

        if (pet.getNome() == null || pet.getNome().trim().isEmpty()) {
            return ResponseEntity.status(400).build();
        }
        if (pet.getIdade() < 0) {
            return ResponseEntity.status(400).build();
        }
        if (pet.getPeso() <= 0) {
            return ResponseEntity.status(400).build();
        }
        if (pet.getAltura() <= 0) {
            return ResponseEntity.status(400).build();
        }

        Pet petNovo = new Pet();
        petNovo.setNome(pet.getNome());
        petNovo.setIdade(pet.getIdade());
        petNovo.setPeso(pet.getPeso());
        petNovo.setAltura(pet.getAltura());

        if (pet.getImagem() != null && !pet.getImagem().isEmpty()) {
            try {
                byte[] imagemBytes = Base64.getDecoder().decode(pet.getImagem());
                petNovo.setImagem(new String(imagemBytes));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(400).build();
            }
        }

        Pet petCadastrado = repository.save(petNovo);
        return ResponseEntity.status(201).body(petCadastrado);
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> getImagem(@PathVariable Integer id) {
        Pet pet = repository.findById(id).orElse(null);
        if (pet == null || pet.getImagem() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Base64.getDecoder().decode(pet.getImagem());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }
}
