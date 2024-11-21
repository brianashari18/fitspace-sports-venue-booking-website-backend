package fitspace.fitspace_sports_venue_booking_website_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpToEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + "\nThis code is valid for 10 minutes.");

        try {
            mailSender.send(message);
            System.out.println("OTP sent to email: " + email);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

