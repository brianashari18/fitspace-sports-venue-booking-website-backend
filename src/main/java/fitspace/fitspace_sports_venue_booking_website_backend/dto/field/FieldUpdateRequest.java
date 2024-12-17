package fitspace.fitspace_sports_venue_booking_website_backend.dto.field;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.fieldschedule.FieldScheduleUpdateRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoUpdateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldUpdateRequest {

    private Long price;

    private String type;

    private List<PhotoUpdateRequest> gallery;

    private List<FieldScheduleUpdateRequest> fieldSchedules;

}
