package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.user.*;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.EnvHelper;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.UserRepository;
import fitspace.fitspace_sports_venue_booking_website_backend.security.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private EmailService emailService;

    private EnvHelper envHelper;

    @Transactional
    public TokenResponse login(UserLoginRequest request) {
        validationService.validate(request);

        User user = userRepository.findFirstByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            return this.setToken(user);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password wrong");
        }
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

    private TokenResponse setToken(User user) {
        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(LocalDateTime.now().plusHours(5));
        userRepository.save(user);

        return TokenResponse.builder()
                .token(user.getToken())
                .expiredAt(user.getTokenExpiredAt())
                .build();
    }

    public void logout(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access token is required for logout");
        }

        if (isGoogleToken(accessToken)) {
            revokeGoogleToken(accessToken);
        } else {
            revokeManualToken(accessToken);
        }
    }

    private boolean isGoogleToken(String accessToken) {
        return accessToken.startsWith("ya29.");
    }

    private void revokeGoogleToken(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String revokeUrl = "https://oauth2.googleapis.com/revoke";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("token", accessToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(revokeUrl, request, String.class);

            log.info("Access Token: {}", accessToken);

            User user = userRepository.findFirstByToken(accessToken)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            log.info("User: {}", user.getEmail());

            user.setToken(null);
            user.setTokenExpiredAt(null);
            userRepository.save(user);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to revoke Google token");
            }
        } catch (Exception e) {
            log.error("Error during Google logout: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during Google logout", e);
        }
    }

    private void revokeManualToken(String accessToken) {
        User user = userRepository.findFirstByToken(accessToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setToken(null);
        user.setTokenExpiredAt(null);
        userRepository.save(user);
    }

}
