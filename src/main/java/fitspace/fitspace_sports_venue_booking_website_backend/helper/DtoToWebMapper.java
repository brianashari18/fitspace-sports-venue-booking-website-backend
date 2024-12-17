package fitspace.fitspace_sports_venue_booking_website_backend.helper;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.WebResponse;
import org.springframework.http.HttpStatus;

public class DtoToWebMapper {
    public static <T> WebResponse<T> toWebResponse(T data) {
        return WebResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(data)
                .build();
    }
}
