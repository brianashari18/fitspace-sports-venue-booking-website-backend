package fitspace.fitspace_sports_venue_booking_website_backend.controller;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.venue.VenueAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.venue.VenueDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.venue.VenueUpdateRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.WebResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.DtoToWebMapper;
import fitspace.fitspace_sports_venue_booking_website_backend.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VenueController {

    @Autowired
    private VenueService venueService;

    @PostMapping(
            path = "/api/venues",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<VenueDataResponse> create(User user, @RequestBody VenueAddRequest request) {
        VenueDataResponse venueDataResponse = venueService.create(user, request);
        return DtoToWebMapper.toWebResponse(venueDataResponse);
    }

    @GetMapping(
            path = "/api/venues/{venueId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<VenueDataResponse> get(@PathVariable("venueId") Integer venueId) {
        VenueDataResponse venueDataResponse = venueService.get(venueId);
        return DtoToWebMapper.toWebResponse(venueDataResponse);
    }

    @PatchMapping(
            path = "/api/venues/{venueId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<VenueDataResponse> update(User user, @PathVariable("venueId") Integer venueId, @RequestBody VenueUpdateRequest request) {
        VenueDataResponse venueDataResponse = venueService.update(user, venueId, request);
        return DtoToWebMapper.toWebResponse(venueDataResponse);
    }

    @DeleteMapping(
            path = "/api/venues/{venueId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable("venueId") Integer venueId) {
        venueService.delete(user, venueId);
        return DtoToWebMapper.toWebResponse("Successfully deleted venue");
    }

    @GetMapping(
            path = "/api/venues",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<VenueDataResponse>> getAll() {
        List<VenueDataResponse> venueDataResponses = venueService.getAll();
        return DtoToWebMapper.toWebResponse(venueDataResponses);
    }

    @GetMapping(
            path = "/api/venues/owner",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<VenueDataResponse>> getAllFromOwner(User user) {
        List<VenueDataResponse> venueDataResponses = venueService.getAllFromOwner(user);
        return DtoToWebMapper.toWebResponse(venueDataResponses);
    }
}
