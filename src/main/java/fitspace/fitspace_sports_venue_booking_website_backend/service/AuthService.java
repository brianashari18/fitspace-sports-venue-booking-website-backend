package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.model.*;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.UserRepository;
import fitspace.fitspace_sports_venue_booking_website_backend.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public TokenResponse login(UserLoginRequest request) {
        validationService.validate(request);

        User user = userRepository.findFirstByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(LocalDateTime.now().plusHours(5));
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password wrong");
        }
    }

    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);
        userRepository.save(user);
    }

    public void forgotPassword(UserForgotPasswordRequest request) {
        validationService.validate(request);

        User user = userRepository.findFirstByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not exist"));

        String otp = String.format("%04d", (int) (Math.random() * 10000));
        user.setOtp(otp);
        user.setOtpExpiredAt(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        emailService.sendOtpToEmail(user.getEmail(), otp);
    }

    public TokenResponse validateOtp(UserValidateOtpRequest request) {
        validationService.validate(request);

        User user = userRepository.findFirstByOtp(request.getOtp())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP"));

        if (user.getOtpExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired");
        }

        user.setOtp(null);
        user.setOtpExpiredAt(null);

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiredAt(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        return TokenResponse.builder()
                .token(user.getResetToken())
                .expiredAt(user.getResetTokenExpiredAt())
                .build();
    }


    @Transactional
    public void resetPassword(UserResetPasswordRequest request, User user) {
        validationService.validate(request);

        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        userRepository.save(user);
        user.setResetToken(null);
        user.setResetTokenExpiredAt(null);
    }
}
