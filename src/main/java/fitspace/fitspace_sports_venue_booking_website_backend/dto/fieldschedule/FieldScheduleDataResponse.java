package fitspace.fitspace_sports_venue_booking_website_backend.dto.fieldschedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldScheduleDataResponse {

    private Long id;

    private String status;

    @JsonProperty("field_id")
    private Long fieldId;

    @JsonProperty("schedule")
    private ScheduleDataResponse schedule;

}
