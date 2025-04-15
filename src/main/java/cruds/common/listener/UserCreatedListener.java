package cruds.common.listener;

import cruds.common.event.UserCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedListener {

    private final JavaMailSender mailSender;

    @Autowired
    public UserCreatedListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        String emailDestinatario = event.getUser().getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDestinatario);
        message.setSubject("Bem-vindo!");
        message.setText("Obrigado por se cadastrar.");

        mailSender.send(message);
    }
}