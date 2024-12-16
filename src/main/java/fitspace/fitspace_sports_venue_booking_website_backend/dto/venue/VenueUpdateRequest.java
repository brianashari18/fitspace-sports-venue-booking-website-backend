package fitspace.fitspace_sports_venue_booking_website_backend.dto.venue;

import com.fasterxml.jackson.annotation.JsonProperty;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoUpdateRequest;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VenueUpdateRequest {

    @Size(max = 100)
    private String name;

    @Pattern(regexp = "\\+?[0-9]*")
    @Size(max = 100)
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Size(max = 255)
    private String street;

    @Size(max = 100)
    private String district;

    @Size(max = 100)
    @JsonProperty("city_or_regency")
    private String cityOrRegency;

    @Size(max = 100)
    private String province;

    @Size(max = 10)
    @JsonProperty("postal_code")
    private String postalCode;

    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;

}
