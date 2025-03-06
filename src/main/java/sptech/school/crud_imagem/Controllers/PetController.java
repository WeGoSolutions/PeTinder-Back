package sptech.school.crud_imagem.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.crud_imagem.Tables.Pet;
import sptech.school.crud_imagem.DTOs.PetDTO;
import sptech.school.crud_imagem.Repositorys.PetRepository;

import java.util.Base64;
import java.util.List;

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
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(pet.getImagem());
    }

    @GetMapping
    public ResponseEntity<List<Pet>> listarGeral(){
        List<Pet> all = repository.findAll();

        if (all.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(all);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> atualizar(@PathVariable Integer id, @RequestBody PetDTO dto){

        if (repository.existsById(id)){
            Pet petParaAlterar = repository.findById(id).orElse(null);
            if (petParaAlterar == null) {
                return ResponseEntity.status(404).build();
            }

            petParaAlterar.setNome(dto.getNome());
            petParaAlterar.setIdade(dto.getIdade());
            petParaAlterar.setPeso(dto.getPeso());
            petParaAlterar.setAltura(dto.getAltura());

            if (dto.getImagemBase64() != null && !dto.getImagemBase64().isEmpty()) {
                try {
                    byte[] imagemBytes = Base64.getDecoder().decode(dto.getImagemBase64());
                    petParaAlterar.setImagem(imagemBytes);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(400).build();
                }
            }

            Pet petAlterado = repository.save(petParaAlterar);
            return ResponseEntity.status(202).body(petAlterado);
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pet> deletarPet(@PathVariable Integer id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            return ResponseEntity.status(205).build();
        }else {
            return ResponseEntity.status(404).build();
        }
    }
}