package cruds.Users.V2.core.adapter;

import org.springframework.stereotype.Service;

@Service
public class EmailGatewayImpl implements EmailGateway {
    @Override
    public void enviarEmailBoasVindas(String email, String nome) {
        // TODO: Implementar envio de e-mail de boas-vindas
    }

    @Override
    public void enviarEmail(String destinatario, String assunto, String conteudo) {
        // TODO: Implementar envio de e-mail genérico
    }
}

