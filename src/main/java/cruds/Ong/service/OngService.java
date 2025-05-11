package cruds.Ong.service;

import cruds.Ong.controller.dto.request.OngRequestCriarDTO;
import cruds.Ong.controller.dto.response.OngResponseLoginDTO;
import cruds.Ong.entity.Ong;
import cruds.Ong.repository.OngRepository;
import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.common.event.UserLoggedInEvent;
import cruds.common.exception.ConflictException;
import cruds.config.token.GerenciadorTokenJwt;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OngService {

    private AuthenticationManager authenticationManager;
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private OngRepository ongRepository;

    public Ong criarOng(@Valid OngRequestCriarDTO dto) {
        if (ongRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("Email ja cadastrado");
        }
        Ong ong = OngRequestCriarDTO.toEntity(dto);
        Ong ongCriada = ongRepository.save(ong);
        return ongCriada;
    }

    public OngResponseLoginDTO login(@Email @NotBlank String email, @NotBlank String senha) {

        Optional<Ong> ongOptional = ongRepository.findByEmailandSenha(email, senha);

        if (ongOptional.isEmpty()) {
            throw new ConflictException("Email ou senha invalidos");
        }

        Ong ong = ongOptional.get();

        final UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(email, senha);
        final Authentication authentication = authenticationManager.authenticate(credentials);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = gerenciadorTokenJwt.generateToken(authentication);


        return OngResponseLoginDTO.builder()
                .id(ong.getId())
                .nome(ong.getNome())
                .email(ong.getEmail())
                .build();
    }


}
