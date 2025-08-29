package cruds.Users.V2.infrastructure.external;

import cruds.Users.V2.core.adapter.EmailGateway;
import cruds.common.service.EmailService;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceAdapter implements EmailGateway {

    private final EmailService emailService;

    public EmailServiceAdapter(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void enviarEmailBoasVindas(String email, String nome) {
        String assunto = "Bem-vindo ao PeTinder, %s!".formatted(nome);
        String conteudo = """
                    <div style="font-family: Arial, sans-serif; background-color: #fefefe; padding: 20px; border-radius: 10px; border: 1px solid #ddd;">
                        <h1 style="color: #ff6f61;">🐾 Bem-vindo ao PeTinder, %s!</h1>
                
                        <p style="font-size: 16px; color: #333;">
                            Estamos super felizes por ter você com a gente! <br>
                            Aqui no <strong>PeTinder</strong>, acreditamos que todo pet merece um lar cheio de amor, e toda pessoa merece um pet que mude sua vida. 💕
                        </p>
                
                        <p style="font-size: 16px; color: #333;">
                            Prepare-se para conhecer novos amigos peludos, descobrir histórias emocionantes e, quem sabe, encontrar seu novo companheiro de quatro patas.
                        </p>
                
                        <p style="font-size: 14px; color: #666;">Com carinho,<br>Equipe PeTinder 🐶🐱</p>
                    </div>
                """.formatted(nome);
        
        emailService.enviarEmail(email, assunto, conteudo);
    }

    @Override
    public void enviarEmail(String destinatario, String assunto, String conteudo) {
        emailService.enviarEmail(destinatario, assunto, conteudo);
    }
}
