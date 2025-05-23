package cruds.Pets.service;

import cruds.Imagem.repository.ImagemRepository;
import cruds.Ong.entity.Ong;
import cruds.Ong.repository.OngRepository;
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
import cruds.common.strategy.ImageStorageStrategy;
import cruds.common.util.ImageValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private ImagemRepository imagemRepository;

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Desktop/S3 local/imagens/";

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ImageStorageStrategy imageStorageStrategy;

    @Autowired
    private OngRepository ongRepository;

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
        Ong ong = ongRepository.findById(dto.getOngId())
                .orElseThrow(() -> new NotFoundException("ONG com id " + dto.getOngId() + " não encontrada"));
        pet.setOng(ong);

        pet = petRepository.save(pet);

        List<String> nomesArquivos = new ArrayList<>();
        List<byte[]> imagensBytes = new ArrayList<>();

        for (String imagemBase64 : dto.getImagemBase64()) {
            try {
                byte[] imagemBytes = decodeImage(imagemBase64);
                imagensBytes.add(imagemBytes);
                nomesArquivos.add("pet_" + UUID.randomUUID() + ".jpg"); // Nome único
            } catch (IllegalArgumentException e) {
                throw new ConflictException("Imagem inválida", e);
            }
        }

        try {
            ImageValidationUtil.validatePetImages(imagensBytes, nomesArquivos);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao validar as imagens: " + e.getMessage());
        }

        List<Imagem> imagens = new ArrayList<>();
        for (int i = 0; i < imagensBytes.size(); i++) {
            try {
                String caminho = imageStorageStrategy.gerarCaminho(nomesArquivos.get(i));
                imageStorageStrategy.salvarImagem(imagensBytes.get(i), nomesArquivos.get(i));
                Imagem imagem = new Imagem(caminho, pet);
                imagens.add(imagem);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar a imagem: " + e.getMessage());
            }
        }

        pet.setImagens(imagens);

        imagemRepository.saveAll(imagens);

        return pet;
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
        for (int i = 0; i < imagensBytes.size(); i++) {
            String filePath = UPLOAD_DIR + "/pet_" + UUID.randomUUID() + ".jpg";
            try {
                salvarImagemNoDisco(imagensBytes.get(i), filePath);
                imagens.add(new Imagem(filePath, petParaAlterar));
            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage());
            }
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
        for (int i = 0; i < imagensBytes.size(); i++) {
            String filePath = UPLOAD_DIR + "/pet_" + UUID.randomUUID() + ".jpg";
            try {
                salvarImagemNoDisco(imagensBytes.get(i), filePath);
                imagens.add(new Imagem(filePath, pet));
            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage());
            }
        }

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
        try {
            return Files.readAllBytes(Paths.get(imagens.get(indice).getCaminho()));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler imagem do disco: " + e.getMessage());
        }

    }

    public Pet curtirPet(Integer id, PetRequestCurtirDTO dto) {
        Pet petParaAlterar = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet com id: " + id + " não encontrado"));
        petParaAlterar.setIsLiked(dto.getIsLiked());
        if (petParaAlterar.getIsLiked()) {
            petParaAlterar.setCurtidas(petParaAlterar.getCurtidas() + 1);
        } else {
            petParaAlterar.setCurtidas(petParaAlterar.getCurtidas() - 1);
            if (petParaAlterar.getCurtidas() <= 0) {
                petParaAlterar.setCurtidas(0);
            }
        }
        return petRepository.save(petParaAlterar);
    }

    public void apagarImagem(Integer id, int indice) {
        Pet pet = obterPetPorId(id);
        List<Imagem> imagens = pet.getImagens();

        if (imagens == null || imagens.isEmpty()) {
            throw new NotFoundException("Nenhuma imagem encontrada para o pet com id " + id);
        }

        if (indice < 0 || indice >= imagens.size()) {
            throw new NotFoundException("Índice " + indice + " inválido para o pet com id " + id
                    + ". Total de imagens: " + imagens.size());
        }

        imagens.remove(indice);
        pet.setImagens(imagens);
        petRepository.save(pet);
    }

    private void salvarImagemNoDisco(byte[] imagemBytes, String caminhoRelativo) throws IOException {
        imageStorageStrategy.salvarImagem(imagemBytes, caminhoRelativo);
    }

}