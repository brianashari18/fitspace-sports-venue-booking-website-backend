package fitspace.fitspace_sports_venue_booking_website_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Photo;
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
public class VenueAddRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Pattern(regexp = "\\+?[0-9]*")
    @Size(max = 100)
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank
    @Size(max = 255)
    private String street;

    @NotBlank
    @Size(max = 100)
    private String district;

    @NotBlank
    @Size(max = 100)
    @JsonProperty("city_or_regency")
    private String cityOrRegency;

    @NotBlank
    @Size(max = 100)
    private String province;

    @NotBlank
    @Size(max = 100)
    private String country;

    @NotBlank
    @Size(max = 10)
    @JsonProperty("postal_code")
    private String postalCode;

    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;

    @NotEmpty
    private List<@NotNull PhotoAddRequest> photoAddRequests;
}
