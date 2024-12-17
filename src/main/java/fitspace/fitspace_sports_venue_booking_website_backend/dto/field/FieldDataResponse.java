package fitspace.fitspace_sports_venue_booking_website_backend.dto.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.fieldschedule.FieldScheduleDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldDataResponse {

    private Long id;

    private Long price;

    private String type;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("venue_id")
    private Long venueId;

    private List<ReviewDataResponse> reviews;

    private List<PhotoDataResponse> gallery;

    private List<FieldScheduleDataResponse> fieldSchedules;

}
