package fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDataResponse {

    private Long id;

    private String status;

    private String price;

    private String name;

    @JsonProperty("customer_id")
    private long customerId;

    @JsonProperty("schedule_id")
    private long scheduleId;

}
