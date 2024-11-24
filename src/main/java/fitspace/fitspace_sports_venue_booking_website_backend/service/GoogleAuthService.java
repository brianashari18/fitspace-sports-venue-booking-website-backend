package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.model.TokenResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.model.UserDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Service
public class GoogleAuthService {

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    public String generateAuthUrl() {
        String authUrl = UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile email")
                .toUriString();
        return authUrl;
    }

    public String getAccessToken(String code) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("code", code);
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("redirect_uri", redirectUri);
            body.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://oauth2.googleapis.com/token", request, Map.class);

            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get access token from Google", e);
        }
    }


    public Map<String, Object> getUserProfile(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo", HttpMethod.GET, request, Map.class);

        return response.getBody();
    }

    public TokenResponse findOrCreateUser(Map<String, Object> profile) {
        if (profile == null || profile.isEmpty()) {
            throw new IllegalArgumentException("Profile data cannot be null or empty");
        }

        String email = (String) profile.get("email");
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        String firstName = (String) profile.get("given_name");
        String lastName = (String) profile.get("family_name");

        User user = userRepository.findFirstByEmail(email)
                .orElseGet(() -> createNewUser(email, firstName, lastName));

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(LocalDateTime.now().plusHours(5));
        userRepository.save(user);

        return TokenResponse.builder()
                .token(user.getToken())
                .expiredAt(user.getTokenExpiredAt())
                .build();
    }

    private User createNewUser(String email, String firstName, String lastName) {
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName != null ? firstName : "");
        user.setLastName(lastName != null ? lastName : "");
        user.setPassword(UUID.randomUUID().toString()); // Generate password default
        return user;
    }

}
