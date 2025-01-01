package fitspace.fitspace_sports_venue_booking_website_backend.dto.field;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoAddRequest;
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
public class FieldAddRequest {

    @NotNull
    private Long price;

    @NotBlank
    private String type;

}
