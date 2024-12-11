package fitspace.fitspace_sports_venue_booking_website_backend.dto.photo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoDataResponse {

    private Integer id;

    @JsonProperty("photo_url")
    private String photoUrl;

    private String description;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("field_id")
    private Integer fieldId;

}
