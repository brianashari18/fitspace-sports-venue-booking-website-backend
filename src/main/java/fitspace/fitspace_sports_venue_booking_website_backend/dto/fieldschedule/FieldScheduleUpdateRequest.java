package fitspace.fitspace_sports_venue_booking_website_backend.dto.fieldschedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldScheduleUpdateRequest {

    private String status;

}
