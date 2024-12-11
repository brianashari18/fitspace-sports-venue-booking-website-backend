package fitspace.fitspace_sports_venue_booking_website_backend.controller;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.user.TokenResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.WebResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.service.GoogleAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/auth/login/google")
public class GoogleAuthController {

    @Autowired
    private GoogleAuthService googleAuthService;

    @GetMapping
    public void initiateGoogleLogin(HttpServletResponse response) throws IOException {
        String authUrl = googleAuthService.generateAuthUrl();
        response.sendRedirect(authUrl);
    }

    @GetMapping("/callback")
    public WebResponse<TokenResponse> handleGoogleCallback(@RequestParam("code") String code) {
        String accessToken = googleAuthService.getAccessToken(code);
        Map<String, Object> profile = googleAuthService.getUserProfile(accessToken);
        TokenResponse tokenResponse = googleAuthService.findOrCreateUser(profile);
        return WebResponse.<TokenResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(tokenResponse)
                .build();
    }
}
