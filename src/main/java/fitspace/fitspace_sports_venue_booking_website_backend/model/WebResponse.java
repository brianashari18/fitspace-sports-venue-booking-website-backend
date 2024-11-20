package fitspace.fitspace_sports_venue_booking_website_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {
    private int code;
    private String status;
    private T data;
    private String errors;
}
