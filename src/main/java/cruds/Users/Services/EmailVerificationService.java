/*package cruds.Users.Services;

import com.sun.mail.smtp.SMTPTransport;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import org.xbill.DNS.*;
import javax.mail.*;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.lang.reflect.Method;

@Service
public class EmailVerificationService {

    private static final int SMTP_TIMEOUT = 10000;

    Lookup lookup = new Lookup(domain, Type.MX);
    org.xbill.DNS.Record[] records = lookup.run();
    public boolean isEmailValid(String email) {
        if (!isValidEmailFormat(email)) return false;

        String domain = extractDomain(email);
        List<String> mxHosts = resolveMxRecords(domain);
        if (mxHosts.isEmpty()) return false;

        return verifyWithSmtp(mxHosts, email);
    }

    private boolean isValidEmailFormat(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            System.err.println("Formato inválido: " + email);
            return false;
        }
        return true;
    }

    private String extractDomain(String email) {
        return email.substring(email.indexOf('@') + 1);
    }

    private List<String> resolveMxRecords(String domain) {
        try {
            Lookup lookup = new Lookup(domain, Type.MX);
            Record[] records = lookup.run();
            if (records == null || records.length == 0) {
                System.err.println("Sem registros MX para: " + domain);
                return Collections.emptyList();
            }

            return processMxRecords(records);
        } catch (TextParseException e) {
            System.err.println("Erro no parse MX: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<String> processMxRecords(org.xbill.DNS.Record[] records) {
        List<String> hosts = new ArrayList<>();
        for (org.xbill.DNS.Record record : records) {
            if (record instanceof org.xbill.DNS.MXRecord) {
                String host = ((org.xbill.DNS.MXRecord) record).getTarget().toString();
                hosts.add(host.replaceAll("\\.$", "")); // Remove trailing dot
            }
        }
        return hosts;
    }

    private boolean verifyWithSmtp(List<String> mxHosts, String email) {
        Properties props = new Properties();
        props.put("mail.smtp.timeout", SMTP_TIMEOUT);
        props.put("mail.smtp.connectiontimeout", SMTP_TIMEOUT);

        Session session = Session.getInstance(props);

        for (String host : mxHosts) {
            try {
                if (testSmtpConnection(session, host, email)) {
                    return true;
                }
            } catch (MessagingException e) {
                System.err.println("Erro SMTP em " + host + ": " + e.getMessage());
            }
        }
        return false;
    }

    private boolean testSmtpConnection(Session session, String host, String email) throws MessagingException {
        Transport transport = null;
        try {
            transport = session.getTransport("smtp");
            transport.connect(host, 25, null, null);

            // Verificação alternativa ao VRFY
            return checkEmailViaRcpt(transport, email);
        } finally {
            if (transport != null) transport.close();
        }
    }

    private boolean checkEmailViaRcpt(Transport transport, String email) throws MessagingException {
        try {
            Message msg = new MimeMessage((Session) null);
            msg.setFrom(new InternetAddress("noreply@example.com"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            Method rcptToMethod = SMTPTransport.class.getDeclaredMethod("rcptTo", Address[].class);
            rcptToMethod.setAccessible(true);
            rcptToMethod.invoke(transport, (Object) msg.getRecipients(Message.RecipientType.TO));
            return true;
        } catch (AddressException e) {
            return false;
        } catch (Exception e) {
            throw new MessagingException("Reflection error", e);
        }
    }
}*/