package vn.nhan.util;

import java.io.InputStream;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil_24110291 {
    private static final Properties CFG = new Properties();

    static {
        try (InputStream in = MailUtil_24110291.class.getClassLoader().getResourceAsStream("mail.properties")) {
            CFG.load(in);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void sendOtp(String to, String otp) throws Exception {
        if (!Boolean.parseBoolean(CFG.getProperty("mail.enabled", "false"))) {
            System.out.println("[OTP - mail.enabled=false] gui toi " + to + " : " + otp);
            return;
        }
        Properties p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", CFG.getProperty("mail.host"));
        p.put("mail.smtp.port", CFG.getProperty("mail.port"));
        final String user = CFG.getProperty("mail.username");
        final String pass = CFG.getProperty("mail.password");
        Session session = Session.getInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(user));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject("Ma OTP kich hoat tai khoan", "UTF-8");
        msg.setText("Ma OTP cua ban la: " + otp + " (het han sau 5 phut).", "UTF-8");
        Transport.send(msg);
    }
}
