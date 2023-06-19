package ru.script_dev.zeta.helpers;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHelper {

    private static final String Host = "smtp.mail.ru";
    private static final String User = "zeta.shop@internet.ru";
    private static final String Password = "XiACbCFr69tPjZuHL9t0";

    public static void sendMail(final String recipient, final String title, final String text) {
        Thread thread = new Thread(() -> {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", Host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", Host);

            Authenticator authenticator = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(User, Password);
                }
            };

            Session session = Session.getInstance(props, authenticator);

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(User));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject(title);
                message.setText(text);

                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}