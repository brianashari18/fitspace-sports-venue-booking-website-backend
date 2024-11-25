package fitspace.fitspace_sports_venue_booking_website_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoDataResponse {

    private Integer id;

    @JsonProperty("photo_url")
    private String photoUrl;

    private String description;

    @JsonProperty("venue_id")
    private Integer venueId;
}
