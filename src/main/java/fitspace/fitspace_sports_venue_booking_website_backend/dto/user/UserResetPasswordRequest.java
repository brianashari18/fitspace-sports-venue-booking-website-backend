package fitspace.fitspace_sports_venue_booking_website_backend.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import fitspace.fitspace_sports_venue_booking_website_backend.validation.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatches
public class UserResetPasswordRequest {

    @JsonProperty("current_password")
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 100)
    @JsonProperty("new_password")
    private String password;

    @NotBlank
    @Size(min = 8, max = 100)
    @JsonProperty("confirmation_password")
    private String confirmationPassword;
}
