package cruds.Pets.service;

import cruds.Pets.controller.dto.request.PetRequestCriarDTO;
import cruds.Pets.controller.dto.request.PetRequestCurtirDTO;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Imagem.entity.Imagem;
import cruds.Pets.entity.Pet;
import cruds.Pets.repository.PetRepository;
import cruds.common.exception.BadRequestException;
import cruds.common.exception.ConflictException;
import cruds.common.exception.NoContentException;
import cruds.common.exception.NotFoundException;
import cruds.common.util.ImageValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    private byte[] decodeImage(String imageData) {
        String base64Data = imageData;
        if (base64Data.startsWith("data:")) {
            int commaIndex = base64Data.indexOf(",");
            if (commaIndex != -1) {
                base64Data = base64Data.substring(commaIndex + 1);
            }
        }
        return Base64.getDecoder().decode(base64Data);
    }

    public Pet cadastrarPet(PetRequestCriarDTO dto) {
        Pet pet = PetRequestCriarDTO.toEntity(dto);

        List<byte[]> imagensBytes = new ArrayList<>();
        List<String> nomesArquivos = new ArrayList<>();
        for (int i = 0; i < dto.getImagemBase64().size(); i++) {
            try {
                byte[] imagemBytes = decodeImage(dto.getImagemBase64().get(i));
                imagensBytes.add(imagemBytes);
                nomesArquivos.add("imagem_" + i + ".jpg");
            } catch (IllegalArgumentException e) {
                throw new ConflictException("Imagem inválida", e);
            }
        }

        try {
            ImageValidationUtil.validatePetImages(imagensBytes, nomesArquivos);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao processar as imagens: " + e.getMessage());
        }

        List<Imagem> imagens = new ArrayList<>();
        for (byte[] imagemBytes : imagensBytes) {
            imagens.add(new Imagem(imagemBytes, null));
        }
        pet.setImagens(imagens);

        return petRepository.save(pet);
    }

    public Pet atualizar(Integer id, PetRequestCriarDTO dto) {
        if (!petRepository.existsById(id)) {
            throw new NotFoundException("Pet com id " + id + " não encontrado");
        }
        Pet petParaAlterar = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet com id " + id + " não encontrado"));

        petParaAlterar = petParaAlterar.toBuilder()
                .nome(dto.getNome())
                .idade(dto.getIdade())
                .peso(dto.getPeso())
                .altura(dto.getAltura())
                .curtidas(dto.getCurtidas())
                .tags(dto.getTags())
                .descricao(dto.getDescricao())
                .build();

        List<byte[]> imagensBytes = new ArrayList<>();
        List<String> nomesArquivos = new ArrayList<>();
        for (int i = 0; i < dto.getImagemBase64().size(); i++) {
            try {
                byte[] imagemBytes = decodeImage(dto.getImagemBase64().get(i));
                imagensBytes.add(imagemBytes);
                nomesArquivos.add("imagem_" + i + ".jpg");
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Imagem inválida", e);
            }
        }

        try {
            ImageValidationUtil.validatePetImages(imagensBytes, nomesArquivos);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao processar as imagens: " + e.getMessage());
        }

        List<Imagem> imagens = new ArrayList<>();
        for (byte[] imagemBytes : imagensBytes) {
            imagens.add(new Imagem(imagemBytes, null));
        }
        petParaAlterar.setImagens(imagens);

        return petRepository.save(petParaAlterar);
    }

    public Pet uploadPetImages(Integer id, List<byte[]> imagensBytes, List<String> nomesArquivos) {
        Pet pet = obterPetPorId(id);
        try {
            ImageValidationUtil.validatePetImages(imagensBytes, nomesArquivos);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao processar as imagens: " + e.getMessage());
        }
        List<Imagem> imagens = new ArrayList<>();
        for (byte[] imagemBytes : imagensBytes) {
            imagens.add(new Imagem(imagemBytes, null));
        }
        pet.setImagens(imagens);
        return petRepository.save(pet);
    }

    public List<PetResponseGeralDTO> listarGeral() {
        var pets = petRepository.findAll();
        if (pets.isEmpty()) {
            throw new NoContentException("Nenhum pet encontrado");
        }
        List<PetResponseGeralDTO> responseList = pets.stream()
                .map(PetResponseGeralDTO::toResponse)
                .collect(Collectors.toList());
        return responseList;
    }

    public Pet obterPetPorId(Integer id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new NoContentException("Pet com id: " + id + " não encontrado"));
    }

    public List<String> listarUrlsImagens(HttpServletRequest request, Integer id) {
        Pet pet = obterPetPorId(id);
        List<Imagem> imagens = pet.getImagens();
        if (imagens == null || imagens.isEmpty()) {
            throw new NotFoundException("Nenhuma imagem encontrada para o pet com id " + id);
        }
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < imagens.size(); i++) {
            urls.add(baseUrl + "/pets/" + id + "/imagens/" + i);
        }
        return urls;
    }

    public void deletarPet(Integer id) {
        if (!petRepository.existsById(id)) {
            throw new NotFoundException("Pet com id: " + id + " não encontrado");
        }
        petRepository.deleteById(id);
    }

    public byte[] getImagemPorIndice(Integer id, int indice) {
        Pet pet = obterPetPorId(id);
        List<Imagem> imagens = pet.getImagens();
        if (imagens == null || imagens.isEmpty()) {
            throw new NotFoundException("Nenhuma imagem para o pet com id " + id);
        }
        if (indice < 0 || indice >= imagens.size()) {
            throw new NotFoundException("Índice " + indice + " inválido para o pet com id " + id
                    + ". Total de imagens: " + imagens.size());
        }
        return imagens.get(indice).getDados();
    }

    public Pet curtirPet(Integer id, PetRequestCurtirDTO dto) {
        Pet petParaAlterar = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet com id: " + id + " não encontrado"));
        petParaAlterar.setIsLiked(dto.getIsLiked());
        return petRepository.save(petParaAlterar);
    }
}