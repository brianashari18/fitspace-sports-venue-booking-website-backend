package fitspace.fitspace_sports_venue_booking_website_backend.dto.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Schedule;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Venue;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

public class FieldDataResponse {

    private Integer id;

    private Long price;

    private String type;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("venue_id")
    private Integer venueId;

    private List<PhotoDataResponse> gallery;

    private List<ScheduleDataResponse> schedules;

}
