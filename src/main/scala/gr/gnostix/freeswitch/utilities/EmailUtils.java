package gr.gnostix.freeswitch.utilities;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by rebel on 10/2/15.
 */
public class EmailUtils {

    public static void sendMailOneRecipient(String toEmail, String msg, String subject) throws IOException {
        final String username = "username";
        final String password = "password";
        final String fromEmailAddress = "info@vieras.eu";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmailAddress, "FS-Moni Support"));

            Address[] toAddr = new InternetAddress[1];
            toAddr[0] = new InternetAddress(toEmail);
            message.setRecipients(Message.RecipientType.TO, toAddr);


            message.setSubject(subject);

            MimeBodyPart messagePart = new MimeBodyPart();
            // messagePart.se
            messagePart.setText(msg, "utf-8");
            messagePart.setHeader("Content-Type",
                    "text/html; charset=\"utf-8\"");
            messagePart.setHeader("Content-Transfer-Encoding",
                    "quoted-printable");
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart); // adding message part

            message.setContent(multipart);
            message.setSentDate(new Date());

            Transport.send(message);

            System.out.println("Email send");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
