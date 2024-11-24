package fitspace.fitspace_sports_venue_booking_website_backend.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VenueUpdateRequest {

    @Size(max = 100)
    private String name;

    @Pattern(regexp = "\\+?[0-9]*")
    @Size(max = 100)
    private String phoneNumber;

    @Size(max = 255)
    private String street;

    @Size(max = 100)
    private String district;

    @Size(max = 100)
    private String cityOrRegency;

    @Size(max = 100)
    private String province;

    @Size(max = 100)
    private String country;

    @Size(max = 10)
    private String postalCode;

    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;

}
