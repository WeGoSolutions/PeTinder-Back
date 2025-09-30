package cruds.Ong.V2.infrastructure.gateway;

import cruds.Ong.V2.core.adapter.CriptografiaGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CriptografiaGatewayImpl implements CriptografiaGateway {

    private final PasswordEncoder passwordEncoder;

    public CriptografiaGatewayImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    @Override
    public boolean verificarSenha(String senhaRaw, String senhaCriptografada) {
        return passwordEncoder.matches(senhaRaw, senhaCriptografada);
    }
}
