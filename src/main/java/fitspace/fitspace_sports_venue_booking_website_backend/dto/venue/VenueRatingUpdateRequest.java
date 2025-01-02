package fitspace.fitspace_sports_venue_booking_website_backend.dto.venue;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VenueRatingUpdateRequest {

    @NotBlank
    private Double rating;
}
