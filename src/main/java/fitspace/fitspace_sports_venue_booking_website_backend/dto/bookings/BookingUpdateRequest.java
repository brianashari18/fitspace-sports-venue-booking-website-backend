package fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingUpdateRequest {
    private String status;
}
