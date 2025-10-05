package cruds.Ong.service;

import cruds.Dashboard.repository.DashboardRepository;
import cruds.Imagem.entity.Imagem;
import cruds.Imagem.entity.ImagemOng;
import cruds.Imagem.repository.ImagemOngRepository;
import cruds.Ong.controller.dto.request.OngRequestCriarDTO;
import cruds.Ong.controller.dto.request.OngRequestImagemDTO;
import cruds.Ong.controller.dto.request.OngRequestUpdateDTO;
import cruds.Ong.controller.dto.response.*;
import cruds.Ong.entity.Ong;
import cruds.Ong.repository.OngRepository;
import cruds.Pets.controller.dto.response.OngResponsePetsComImagensDTO;
import cruds.Pets.entity.Pet;
import cruds.Users.controller.dto.request.EnderecoRequestDTO;
import cruds.Users.entity.Endereco;
import cruds.common.exception.NoContentException;
import cruds.common.util.ImageValidationUtil;
import cruds.Pets.entity.PetStatus;
import cruds.Pets.enums.PetStatusEnum;
import cruds.Pets.repository.PetRepository;
import cruds.Pets.repository.PetStatusRepository;
import cruds.Users.entity.User;
import cruds.common.exception.BadRequestException;
import cruds.common.exception.ConflictException;
import cruds.common.exception.NotFoundException;
import cruds.common.strategy.ImageStorageStrategy;
import cruds.config.token.GerenciadorTokenJwt;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class OngService {

    private final PasswordEncoder passwordEncoder;
    private final ImageStorageStrategy imageStorageStrategy;
    private final ImagemOngRepository imagemOngRepository;
    private final DashboardRepository dashboardRepository;
    private AuthenticationManager authenticationManager;
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    private OngRepository ongRepository;
    private final PetRepository petRepository;

    private final PetStatusRepository petStatusRepository;
    private static final String DEFAULT_IMAGE_NAME = "ong.jpg";
    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Desktop/S3 local/imagens/";


    @Autowired
    public OngService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, GerenciadorTokenJwt gerenciadorTokenJwt, OngRepository ongRepository, ImageStorageStrategy imageStorageStrategy, PetRepository petRepository, PetStatusRepository petStatusRepository, ImagemOngRepository imagemOngRepository, DashboardRepository dashboardRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.ongRepository = ongRepository;
        this.imageStorageStrategy = imageStorageStrategy;
        this.petRepository = petRepository;
        this.petStatusRepository = petStatusRepository;
        this.imagemOngRepository = imagemOngRepository;
        this.dashboardRepository = dashboardRepository;
    }

    public Ong criarOng(@Valid OngRequestCriarDTO dto) {
        if (ongRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("Email ja cadastrado");
        }
        Ong ong = OngRequestCriarDTO.toEntity(dto);
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        ong.setSenha(senhaCriptografada);
        Ong ongCriada = ongRepository.save(ong);
        return ongCriada;
    }

    public OngResponseDTO updatePassword(UUID id, String senhaAtual, String novaSenha) {
        Ong ong = ongRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ONG não encontrada"));

        if (!passwordEncoder.matches(senhaAtual, ong.getSenha())) {
            throw new ConflictException("Senha atual não confere");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(novaSenha);
        ong.setSenha(novaSenhaCriptografada);
        ongRepository.save(ong);

        return OngResponseDTO.toResponse(ong);
    }

    public OngResponseLoginDTO login(@Email @NotBlank String email, @NotBlank String senha) {


        Optional<Ong> ongOptional = ongRepository.findByEmail(email);
        if (ongOptional.isEmpty()) {
            throw new NotFoundException("Email não encontrado");
        }

        Ong ong = ongOptional.get();
        if (!passwordEncoder.matches(senha, ong.getSenha())) {
            throw new NotFoundException("Senha inválida");
        }

        return OngResponseLoginDTO.builder()
                .id(ong.getId())
                .nome(ong.getNome())
                .email(ong.getEmail())
                .build();
    }


    public OngResponseDTO getOng(UUID id) {
        Ong ong = acharPorId(id);
        return OngResponseDTO.toResponse(ong);
    }

    public OngResponseDTO updateOng(UUID id, OngRequestUpdateDTO ongRequest) {
        Ong ongExistente = acharPorId(id);
        if (!ongExistente.getEmail().equals(ongRequest.getEmail()) && ongRepository.findByEmail(ongRequest.getEmail()).isPresent()) {
            throw new ConflictException("Email já cadastrado");
        }

        if (ongRequest.getEndereco() != null) {
            EnderecoRequestDTO e = ongRequest.getEndereco();
            if (e.getCep() == null || e.getRua() == null || e.getNumero() == null ||
                    e.getCidade() == null || e.getUf() == null) {
                throw new BadRequestException("Campos de endereço obrigatórios estão faltando");
            }
        }

        Endereco enderecoAtualizado = null;
        if (ongRequest.getEndereco() != null) {
            Endereco enderecoAtual = ongExistente.getEndereco();
            enderecoAtualizado = ongRequest.getEndereco().toEntityWithId(
                    enderecoAtual != null ? enderecoAtual.getId() : null
            );
        } else {
            enderecoAtualizado = ongExistente.getEndereco(); // mantém o endereço atual
        }

        ongExistente = ongExistente.toBuilder()
                .cnpj(ongRequest.getCnpj())
                .cpf(ongRequest.getCpf())
                .nome(ongRequest.getNome())
                .razaoSocial(ongRequest.getRazaoSocial() != null ? ongRequest.getRazaoSocial() : ongExistente.getRazaoSocial())
                .senha(ongRequest.getSenha() != null ? ongRequest.getSenha() : ongExistente.getSenha())
                .email(ongRequest.getEmail())
                .link(ongRequest.getLink())
                .endereco(enderecoAtualizado)
                .build();

        Ong ongAtualizada = ongRepository.save(ongExistente);
        return OngResponseDTO.toResponse(ongAtualizada);
    }


    public Ong acharPorId(UUID id) {
        return ongRepository.findById(id)
                .orElseThrow(() -> new ConflictException("Ong com id:" + id + " não encontrada"));
    }

    public Page<OngResponsePetsComImagensDTO> listarTodosPetsDeOng(UUID ongId, Pageable pageable) {
        if (!ongRepository.existsById(ongId)) {
            throw new EntityNotFoundException("ONG não encontrada com ID: " + ongId);
        }

        Page<Pet> petsPage = petRepository.findByOng_Id(ongId, pageable);

        if (petsPage.isEmpty()) {
            throw new NoContentException("Nenhum pet encontrado para esta ONG");
        }

        return petsPage.map(pet -> {
            List<String> statusList = petStatusRepository.findByPet_Id(pet.getId())
                    .stream()
                    .map(status -> status.getStatus().name())
                    .collect(Collectors.toList());

            List<String> imagensUrls = gerarUrlsImagens(pet.getId(), pet.getImagens());

            return new OngResponsePetsComImagensDTO(ongId, pet, statusList, imagensUrls);
        });
    }

    private List<String> gerarUrlsImagens(UUID petId, List<Imagem> imagens) {
        if (imagens == null || imagens.isEmpty()) {
            return List.of();
        }

        return IntStream.range(0, imagens.size())
                .mapToObj(indice -> String.format("/api/pets/%s/imagens/%d", petId, indice))
                .collect(Collectors.toList());
    }



    public OngResponseUrlDTO getImageOng(UUID id) {
        Ong ong = acharPorId(id);
        if (ong.getImagemOng() == null) {
            throw new ConflictException("Imagem não encontrada");
        }
        return OngResponseUrlDTO.toResponse(ong);
    }

    public List<OngResponseMensagensPendingDTO> listarMensagensPendentes(UUID ongId, HttpServletRequest request) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new NotFoundException("ONG com id " + ongId + " não encontrada"));

        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        List<Pet> petsOng = petRepository.findByOngId(ongId);
        List<OngResponseMensagensPendingDTO> mensagensPendentes = new ArrayList<>();

        for (Pet pet : petsOng) {
            List<PetStatus> statusList = petStatusRepository.findByPet_IdAndStatus(pet.getId(), PetStatusEnum.PENDING);
            for (PetStatus status : statusList) {
                User user = status.getUser();
                mensagensPendentes.add(
                    OngResponseMensagensPendingDTO.toResponse(ongId, pet, user, status.getAlteradoParaPending())
                );
            }
        }
        return mensagensPendentes;
    }

    @Transactional
    public OngResponseUrlDTO updateImagem(UUID id, @Valid OngRequestImagemDTO imagem) {
        byte[] imagemDecodificada = imagem.getImagensBytesDecoded();
        try {
            ImageValidationUtil.validateOngImage(imagemDecodificada, DEFAULT_IMAGE_NAME);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao processar a imagem: " + e.getMessage());
        }
        Ong ong = acharPorId(id);
        String nomeArquivo = "ong_" + id + "_perfil.jpg";
        String caminhoCompleto = UPLOAD_DIR + nomeArquivo;
        try {
            imageStorageStrategy.salvarImagem(imagemDecodificada, caminhoCompleto);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao salvar a imagem: " + e.getMessage());
        }
        if (ong.getImagemOng() != null) {
            ong.getImagemOng().setDados(imagemDecodificada);
            ong.getImagemOng().setArquivo(caminhoCompleto);
        } else {
            ImagemOng imagemOng = new ImagemOng(imagemDecodificada);
            imagemOng.setArquivo(caminhoCompleto);
            ong.setImagemOng(imagemOng);
        }
        Ong updatedOng = ongRepository.save(ong);
        System.out.println("Imagem ID: " + updatedOng.getImagemOng().getId());
        return OngResponseUrlDTO.toResponse(updatedOng);
    }

    public byte[] getImagemPorIndice(UUID id, int indice) {
        Ong ong = acharPorId(id);
        if (ong.getImagemOng() == null || indice != 0) {
            throw new NotFoundException("Imagem não encontrada para o usuário com id " + id);
        }
        return ong.getImagemOng().getDados();
    }

    @Transactional
    public void deletarPorId(UUID id) {
        if (ongRepository.existsById(id)) {
            petRepository.deleteByOngId(id);
            dashboardRepository.deleteByOngId(id);
            ongRepository.deleteById(id);
            return;
        }
        throw new NotFoundException("Ong de id " + id + " não encontrado");
    }
}