package fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Booking;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Field;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDataResponse {

    private Integer id;

    private LocalDate date;

    @JsonProperty("time_slot")
    private String timeSlot;

    private String status = "available";

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("field_id")
    private Integer fieldId;

    @JsonProperty("booking_id")
    private Integer bookingId;

}
