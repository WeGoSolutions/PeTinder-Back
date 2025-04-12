package cruds.Pets.service;

import cruds.Pets.controller.dto.request.PetRequestCriarDTO;
import cruds.Pets.controller.dto.request.PetRequestCurtirDTO;
import cruds.Pets.controller.dto.response.PetResponseCriarDTO;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.entity.Imagem;
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
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<PetResponseGeralDTO> listarGeral() {
        var pets = petRepository.findAll();
        if (pets.isEmpty()) {
            throw new PetVazioException("Nenhum pet encontrado");
        }
        List<PetResponseGeralDTO> responseList = new ArrayList<>();
        for (Pet petDaVez : pets) {
            responseList.add(PetResponseGeralDTO.toResponse(petDaVez));
        }
        if (responseList.isEmpty()){
            throw new PetVazioException("Nenhum pet encontrado");
        }
        return responseList;
    }

    public Pet cadastrarPet(PetRequestCriarDTO dto) {
        Pet pet = PetRequestCriarDTO.toEntity(dto);

        List<Imagem> imagens = new ArrayList<>();
        for (String imagemBase64 : dto.getImagemBase64()) {
            try {
                byte[] imagemBytes = Base64.getDecoder().decode(imagemBase64);
                imagens.add(new Imagem(imagemBytes));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Imagem inválida", e);
            }
        }
        pet.setImagem(imagens);

        return petRepository.save(pet);
    }

    public Pet obterPetPorId(Integer id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new PetVazioException("Pet com id: " + id + " não encontrado"));
    }

    public List<String> listarUrlsImagens(HttpServletRequest request, Integer id) {
        Pet pet = obterPetPorId(id);
        List<Imagem> imagens = pet.getImagem();
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

    public void deletarPet(Integer id) {
        if (!petRepository.existsById(id)) {
            throw new PetNotFoundException("Pet com id: " + id + " não encontrado");
        }
        petRepository.deleteById(id);
    }

    public Pet atualizar(Integer id, PetRequestCriarDTO dto){
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
                .descricao(dto.getDescricao())
                .build();

        List<Imagem> imagens = new ArrayList<>();
        for (String imagemBase64 : dto.getImagemBase64()) {
            try {
                byte[] imagemBytes = Base64.getDecoder().decode(imagemBase64);
                imagens.add(new Imagem(imagemBytes));
            } catch (IllegalArgumentException e) {
                throw new PetBadRequest("Imagem inválida", e);
            }
        }
        petParaAlterar.setImagem(imagens);

        return petRepository.save(petParaAlterar);
    }

    public byte[] getImagemPorIndice(Integer id, int indice) {
        Pet pet = obterPetPorId(id);
        List<Imagem> imagens = pet.getImagem();
        if (imagens == null || imagens.isEmpty()) {
            throw new PetNotFoundException("Nenhuma imagem para o pet com id " + id);
        }
        if (indice < 0 || indice >= imagens.size()) {
            throw new PetNotFoundException("Índice " + indice + " inválido para o pet com id " + id
                    + ". Total de imagens: " + imagens.size());
        }
        return imagens.get(indice).getDados();
    }

    public Pet curtirPet(Integer id, PetRequestCurtirDTO dto) {
        Pet petParaAlterar = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet com id: " + id + " não encontrado"));
        petParaAlterar.setIsLiked(dto.getIsLiked());
        return petRepository.save(petParaAlterar);
    }
}