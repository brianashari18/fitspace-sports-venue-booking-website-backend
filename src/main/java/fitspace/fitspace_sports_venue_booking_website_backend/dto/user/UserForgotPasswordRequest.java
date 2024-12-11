package fitspace.fitspace_sports_venue_booking_website_backend.dto.user;

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
public class UserForgotPasswordRequest {

    @NotBlank
    @Size(max = 100)
    private String email;
}
