package cruds.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import cruds.Pets.Tables.Pet;
import cruds.Pets.DTOs.PetDTO;
import cruds.Pets.Repositorys.PetRepository;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/pets")
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
        if (dto.getCurtidas() == null || dto.getCurtidas() < 0) {
            return ResponseEntity.status(400).build();
        }
        if (dto.getTags() == null || dto.getTags().isEmpty()) {
            return ResponseEntity.status(400).build();
        }

        Pet petNovo = new Pet();
        petNovo.setNome(dto.getNome());
        petNovo.setIdade(dto.getIdade());
        petNovo.setPeso(dto.getPeso());
        petNovo.setAltura(dto.getAltura());
        petNovo.setCurtidas(dto.getCurtidas());
        petNovo.setTags(dto.getTags());

        if (dto.getImagemBase64() != null && !dto.getImagemBase64().isEmpty()) {
            List<byte[]> imagensBytes = new ArrayList<>();
            for (String imagemBase64 : dto.getImagemBase64()) {
                try {
                    byte[] imagemBytes = Base64.getDecoder().decode(imagemBase64);
                    imagensBytes.add(imagemBytes);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(400).build();
                }
            }
            petNovo.setImagem(imagensBytes);
        }

        Pet petCadastrado = repository.save(petNovo);

        return ResponseEntity.status(201).body(petCadastrado);
    }

    @GetMapping("/{id}/imagens")
    public ResponseEntity<List<String>> listarUrlsImagens(HttpServletRequest request, @PathVariable Integer id) {
        Pet pet = repository.findById(id).orElse(null);
        if (pet == null) {
            System.out.println("Pet com id " + id + " não encontrado.");
            return ResponseEntity.status(404).build();
        }

        List<byte[]> imagens = pet.getImagem();
        if (imagens == null || imagens.isEmpty()) {
            System.out.println("Nenhuma imagem encontrada para o pet com id " + id);
            return ResponseEntity.status(404).build();
        }

        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        List<String> urls = new ArrayList<>();
        for (int i = 0; i < imagens.size(); i++) {
            String url = baseUrl + "/pets/" + id + "/imagens/" + i;
            urls.add(url);
        }
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/{id}/imagens/{indice}")
    public ResponseEntity<byte[]> getImagemPorIndice(@PathVariable Integer id, @PathVariable int indice) {
        Pet pet = repository.findById(id).orElse(null);
        if (pet == null) {
            System.out.println("Pet com id " + id + " não encontrado.");
            return ResponseEntity.status(404).build();
        }

        List<byte[]> imagens = pet.getImagem();
        if (imagens == null || imagens.isEmpty()) {
            System.out.println("Nenhuma imagem para o pet com id " + id);
            return ResponseEntity.status(404).build();
        }

        if (indice < 0 || indice >= imagens.size()) {
            System.out.println("Índice " + indice + " inválido para o pet com id " + id + ". Total de imagens: " + imagens.size());
            return ResponseEntity.status(404).build();
        }

        byte[] imagem = imagens.get(indice);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Altere para o tipo correto se não for JPEG
                .body(imagem);
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
            petParaAlterar.setCurtidas(dto.getCurtidas());
            petParaAlterar.setTags(dto.getTags());

            if (dto.getImagemBase64() != null && !dto.getImagemBase64().isEmpty()) {
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
