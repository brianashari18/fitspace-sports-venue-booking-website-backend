package fitspace.fitspace_sports_venue_booking_website_backend.model;

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
public class VenueDataResponse {

    private Integer id;

    private String name;

    private String phoneNumber;

    private String street;

    private String district;

    private String cityOrRegency;

    private String province;

    private String country;

    private String postalCode;

    private Double latitude;

    private Double longitude;

    private Double rating;

    private Integer reviewsCount;

    private Integer ownerId;

    private List<String> field_types;
}
