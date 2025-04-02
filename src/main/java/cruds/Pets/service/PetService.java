package cruds.Pets.service;

import cruds.Pets.controller.dto.PetRequestDTO;
import cruds.Pets.controller.dto.PetResponseDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.exceptions.PetBadRequest;
import cruds.Pets.exceptions.PetNotFoundException;
import cruds.Pets.exceptions.PetVazioException;
import cruds.Pets.repository.PetRepository;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<PetResponseDTO> listarGeral() {
        var pets = petRepository.findAll();
        if (pets.isEmpty()) {
            throw new PetVazioException("Nenhum pet encontrado");
        }
        List<PetResponseDTO> responseList = new ArrayList<>();
        for (Pet petDaVez : pets) {
            responseList.add(PetResponseDTO.toResponse(petDaVez));
        }
        if (responseList.isEmpty()){
            throw new PetVazioException("Nenhum pet encontrado");
        }

        return responseList;
    }

    public Pet cadastrarPet(PetRequestDTO dto) {
        Pet pet = PetRequestDTO.toEntity(dto);

        List<byte[]> imagensBytes = new ArrayList<>();
        for (String imagemBase64 : dto.getImagemBase64()) {
            try {
                byte[] imagemBytes = Base64.getDecoder().decode(imagemBase64);
                imagensBytes.add(imagemBytes);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Imagem inválida", e);
            }
        }
        pet.setImagem(imagensBytes);

        return petRepository.save(pet);
    }

    public Pet obterPetPorId(Integer id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new PetVazioException("Pet com id: " + id + " não encontrado"));
    }

    public List<String> listarUrlsImagens(HttpServletRequest request, Integer id) {
        Pet pet = obterPetPorId(id);
        List<byte[]> imagens = pet.getImagem();
        if (imagens == null || imagens.isEmpty()) {
            throw new PetNotFoundException("Nenhuma imagem encontrada para o pet com id " + id);
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
        return urls;
    }

    public byte[] getImagemPorIndice(Integer id, int indice) {
        Pet pet = obterPetPorId(id);

        List<byte[]> imagens = pet.getImagem();
        if (imagens.isEmpty()) {
            throw new PetNotFoundException("Nenhuma imagem encontrada para o pet com id " + id);
        }

        if (indice < 0 || indice >= imagens.size()) {
            throw new PetNotFoundException("Índice da imagem inválido: " + indice);
        }

        byte[] imagem = imagens.get(indice);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imagem).getBody();
    }

    public void deletarPet(Integer id) {
        if (!petRepository.existsById(id)) {
            throw new PetNotFoundException("Pet com id: " + id + " não encontrado");
        }
        petRepository.deleteById(id);
    }

    public Pet atualizar(Integer id, PetRequestDTO dto){
        if (!petRepository.existsById(id)){
            throw new PetNotFoundException("Pet com id: " + id + " não encontrado");
        }

        Pet petParaAlterar = petRepository.findById(id).orElse(null);
        if (petParaAlterar == null) {
            throw new PetNotFoundException("Pet com id: " + id + " não encontrado");
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
                throw new PetBadRequest("Imagem inválida", e);
            }
        }

        petParaAlterar.setImagem(imagensBytes);

        return petRepository.save(petParaAlterar);
    }
}