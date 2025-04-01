package cruds.Pets.controller;

import cruds.Pets.exceptions.PetNotFoundException;
import cruds.Pets.exceptions.PetVazioException;
import cruds.Pets.service.PetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import cruds.Pets.entity.Pet;
import cruds.Pets.controller.dto.PetRequestDTO;
import cruds.Pets.controller.dto.PetResponseDTO;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<PetResponseDTO> cadastrarPet(@Valid @RequestBody PetRequestDTO dto) {

        var petCadastrado = petService.cadastrarPet(dto);

        return ResponseEntity.status(201).body(PetResponseDTO.toResponse(petCadastrado));
    }

    @GetMapping("/{id}/imagens")
    public ResponseEntity<List<String>> listarUrlsImagens(HttpServletRequest request, @PathVariable Integer id) {
        var urls = petService.listarUrlsImagens(request, id);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/{id}/imagens/{indice}")
    public ResponseEntity<byte[]> getImagemPorIndice(@PathVariable Integer id, @PathVariable int indice) {

        List<byte[]> imagens = pet.getImagem();
        if (imagens.isEmpty()) {
            System.out.println("Nenhuma imagem para o pet com id " + id);
            return ResponseEntity.status(404).build();
        }

        if (indice < 0 || indice >= imagens.size()) {
            System.out.println("Índice " + indice + " inválido para o pet com id " + id + ". Total de imagens: " + imagens.size());
            return ResponseEntity.status(404).build();
        }

        byte[] imagem = imagens.get(indice);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imagem);
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDTO>> listarGeral(){
       var pets = petService.listarPets();

        List<PetResponseDTO> responseList = new ArrayList<>();
        for (Pet petDaVez : pets) {
            responseList.add(PetResponseDTO.toResponse(petDaVez));
        }

        return ResponseEntity.status(200).body(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> atualizar(@PathVariable Integer id, @RequestBody PetRequestDTO dto){

        if (repository.existsById(id)){
            Pet petParaAlterar = repository.findById(id).orElse(null);
            if (petParaAlterar == null) {
                return ResponseEntity.status(404).build();
            }

            petParaAlterar = petParaAlterar.toBuilder()
                    .nome(dto.getNome())
                    .idade(dto.getIdade())
                    .peso(dto.getPeso())
                    .altura(dto.getAltura())
                    .curtidas(dto.getCurtidas())
                    .tags(dto.getTags())
                    .build();

            List<byte[]> imagensBytes = new ArrayList<>();
            for (String imagemBase64 : dto.getImagemBase64()) {
                try {
                    byte[] imagemBytes = Base64.getDecoder().decode(imagemBase64);
                    imagensBytes.add(imagemBytes);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(400).build();
                }
            }
            petParaAlterar.setImagem(imagensBytes);

            Pet petAlterado = repository.save(petParaAlterar);
            return ResponseEntity.status(202).body(PetResponseDTO.toResponse(petAlterado));
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPet(@PathVariable Integer id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            return ResponseEntity.status(205).build();
        }else {
            return ResponseEntity.status(404).build();
        }
    }
}