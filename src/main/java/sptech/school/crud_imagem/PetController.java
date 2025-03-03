package sptech.school.crud_imagem;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> cadastrarPet(@RequestBody Pet pet) {

        if (pet.getNome() == null || pet.getNome().trim().isEmpty()) {
            return ResponseEntity.status(400).body("O campo 'nome' é obrigatório.");
        }
        if (pet.getIdade() < 0) {
            return ResponseEntity.status(400).body("A 'idade' deve ser um valor positivo.");
        }
        if (pet.getPeso() <= 0) {
            return ResponseEntity.status(400).body("O 'peso' deve ser maior que zero.");
        }
        if (pet.getAltura() <= 0) {
            return ResponseEntity.status(400).body("A 'altura' deve ser maior que zero.");
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
                return ResponseEntity.status(400).body("Formato de imagem inválido.");
            }
        }

        Pet petCadastrado = repository.save(petNovo);
        return ResponseEntity.status(201).body(petCadastrado);
    }
}
