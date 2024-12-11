package fitspace.fitspace_sports_venue_booking_website_backend.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Field;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDataResponse {

    private Integer id;

    private Integer rating;

    private String comment;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("field_id")
    private Integer fieldId;

    @JsonProperty("user_id")
    private Integer userId;

}
