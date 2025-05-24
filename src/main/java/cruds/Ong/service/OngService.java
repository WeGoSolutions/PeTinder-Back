package cruds.Ong.service;

import cruds.Imagem.entity.Imagem;
import cruds.Imagem.entity.ImagemOng;
import cruds.Ong.controller.dto.request.OngRequestCriarDTO;
import cruds.Ong.controller.dto.request.OngRequestImagemDTO;
import cruds.Ong.controller.dto.request.OngRequestUpdateDTO;
import cruds.Ong.controller.dto.response.OngResponseDTO;
import cruds.Ong.controller.dto.response.OngResponseLoginDTO;
import cruds.Ong.entity.Ong;
import cruds.Ong.repository.OngRepository;
import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.entity.ImagemUser;
import cruds.Users.entity.User;
import cruds.common.event.UserLoggedInEvent;
import cruds.common.exception.BadRequestException;
import cruds.common.exception.ConflictException;
import cruds.common.exception.NotFoundException;
import cruds.common.strategy.ImageStorageStrategy;
import cruds.common.util.ImageValidationUtil;
import cruds.config.token.GerenciadorTokenJwt;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OngService {

    private final PasswordEncoder passwordEncoder;
    private final ImageStorageStrategy imageStorageStrategy;
    private AuthenticationManager authenticationManager;
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    private OngRepository ongRepository;
    private static final String DEFAULT_IMAGE_NAME = "ong.jpg";
    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Desktop/S3 local/imagens/";


    @Autowired
    public OngService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, GerenciadorTokenJwt gerenciadorTokenJwt, OngRepository ongRepository, ImageStorageStrategy imageStorageStrategy) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.ongRepository = ongRepository;
        this.imageStorageStrategy = imageStorageStrategy;
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


    public OngResponseDTO getOng(Integer id) {
        Ong ong = acharPorId(id);
        return OngResponseDTO.toResponse(ong);
    }

    public OngResponseDTO updateOng(Integer id, OngRequestUpdateDTO ongRequest) {
        Ong ongExistente = acharPorId(id);
        if (!ongExistente.getEmail().equals(ongRequest.getEmail()) && ongRepository.findByEmail(ongRequest.getEmail()).isPresent()) {
            throw new ConflictException("Email já cadastrado");
        }

        ongExistente = ongExistente.toBuilder()
                .cnpj(ongRequest.getCnpj())
                .cpf(ongRequest.getCpf())
                .nome(ongRequest.getNome())
                .razaoSocial(ongRequest.getRazaoSocial())
                .senha(ongRequest.getSenha())
                .email(ongRequest.getEmail())
                .link(ongRequest.getLink())
                .build();

        Ong ongAtualizada = ongRepository.save(ongExistente);
        return OngResponseDTO.toResponse(ongAtualizada);
    }


    @Transactional
    public Ong uploadOngImage(Integer id, byte[] imagemBytes, String nomeArquivo, String extension) {
        Ong ong = acharPorId(id);

        try {
            // Validar a imagem
            ImageValidationUtil.validateOngImage(imagemBytes, nomeArquivo);

            // Gerar um caminho único para a imagem
            String filePath = UPLOAD_DIR + "/ong_" + UUID.randomUUID() + "." + (extension.isEmpty() ? "jpg" : extension);

            // Salvar a imagem no disco
            salvarImagemNoDisco(imagemBytes, filePath);

            // Criar e vincular a imagem à ONG
            ImagemOng imagemOng = new ImagemOng(filePath, ong);
            ong.setImagemOng(imagemOng);

            // Salvar a ONG atualizada
            return ongRepository.save(ong);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao processar a imagem: " + e.getMessage());
        }
    }

    public Ong acharPorId(Integer id){
        return ongRepository.findById(id)
                .orElseThrow(() -> new ConflictException("Ong com id:" + id + " não encontrada"));
    }

    private void salvarImagemNoDisco(byte[] imagemBytes, String caminhoRelativo) throws IOException {
        imageStorageStrategy.salvarImagem(imagemBytes, caminhoRelativo);
    }

    public OngResponseDTO getImageOng(Integer id) {
        Ong ong = acharPorId(id);
        ImagemOng imagemOng = ong.getImagemOng();
        if (imagemOng == null) {
            throw new ConflictException("Imagem não encontrada");
        }
        return OngResponseDTO.toResponse(ong);
    }
}
