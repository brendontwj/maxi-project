package vttp.maxiproject.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String username, String toEmail, String Subject) {

        String body = String.format("Dear %s \n\n", username)
                        + "Welcome to TMDB App, you have successfully registered an account. \n\n"
                        + "This is a system generated email, please do not reply.";

        System.out.println(">>>>>>> email body: " + body);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vttptmdb@gmail.com");
        message.setTo(toEmail);
        message.setSubject(Subject);
        message.setText(body);

        mailSender.send(message);

        System.out.println("Mail sent successfully to " + toEmail);
    }
}
