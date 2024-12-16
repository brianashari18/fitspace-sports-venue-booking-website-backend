package fitspace.fitspace_sports_venue_booking_website_backend.dto.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class FieldAddRequest {

    @NotNull
    private Long price;

    @NotBlank
    private String type;

    private List<PhotoAddRequest> gallery;

}
