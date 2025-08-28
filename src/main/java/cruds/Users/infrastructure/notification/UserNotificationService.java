package cruds.Users.infrastructure.notification;

import cruds.Users.entity.User;
import cruds.common.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Serviço responsável por envio de notificações relacionadas aos usuários.
 * Centraliza toda a lógica de comunicação por email.
 */
@Service
public class UserNotificationService {

    private final EmailService emailService;

    @Autowired
    public UserNotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Envia email de boas-vindas para novo usuário
     */
    public void sendWelcomeEmail(User user) {
        String subject = "Bem-vindo ao PeTinder, %s!".formatted(user.getNome());
        String htmlContent = buildWelcomeEmailContent(user.getNome());
        
        emailService.enviarEmail(user.getEmail(), subject, htmlContent);
    }

    /**
     * Envia notificação de login
     */
    public void sendLoginNotification(User user) {
        LocalDateTime loginTime = LocalDateTime.now();
        
        String subject = "🔒 Novo login no PeTinder, " + user.getNome() + "!";
        String htmlContent = buildLoginNotificationContent(
            user.getNome(), 
            loginTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"))
        );
        
        emailService.enviarEmail(user.getEmail(), subject, htmlContent);
    }

    /**
     * Envia email de redefinição de senha
     */
    public void sendPasswordResetNotification(User user) {
        String subject = "🔐 Senha alterada no PeTinder";
        String htmlContent = buildPasswordResetNotificationContent(user.getNome());
        
        emailService.enviarEmail(user.getEmail(), subject, htmlContent);
    }

    private String buildWelcomeEmailContent(String nome) {
        return """
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
    }

    private String buildLoginNotificationContent(String nome, String dataHora) {
        return """
            <div style="font-family: Arial, sans-serif; background-color: #ffffff; padding: 20px; border-radius: 8px; border:1px solid #e0e0e0;">
              <h2 style="color: #4a90e2;">🔒 Olá, %s!</h2>
              <p style="font-size: 16px; color: #333;">
                Detectamos um <strong>novo acesso</strong> à sua conta em <em>%s</em>.
              </p>
              <p style="font-size: 15px; color: #333;">
                Se foi você, continue aproveitando o PeTinder. 😊<br>
                Caso não reconheça este acesso, <strong>recomendamos</strong> trocar sua senha imediatamente.
              </p>
              <p style="font-size: 14px; color: #777;">
                Abraços,<br>
                Equipe PeTinder 🐶🐱
              </p>
            </div>
            """.formatted(nome, dataHora);
    }

    private String buildPasswordResetNotificationContent(String nome) {
        return """
            <div style="font-family: Arial, sans-serif; background-color: #ffffff; padding: 20px; border-radius: 8px; border:1px solid #e0e0e0;">
              <h2 style="color: #4a90e2;">🔐 Olá, %s!</h2>
              <p style="font-size: 16px; color: #333;">
                Sua senha foi <strong>alterada com sucesso</strong> no PeTinder.
              </p>
              <p style="font-size: 15px; color: #333;">
                Se você não fez esta alteração, entre em contato conosco imediatamente.
              </p>
              <p style="font-size: 14px; color: #777;">
                Equipe PeTinder 🐶🐱
              </p>
            </div>
            """.formatted(nome);
    }
}
