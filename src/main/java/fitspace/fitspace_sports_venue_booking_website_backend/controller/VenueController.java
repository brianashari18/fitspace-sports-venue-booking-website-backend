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
    public WebResponse<VenueDataResponse> get(@PathVariable("venueId") Long venueId) {
        VenueDataResponse venueDataResponse = venueService.get(venueId);
        return DtoToWebMapper.toWebResponse(venueDataResponse);
    }

    @PatchMapping(
            path = "/api/venues/{venueId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<VenueDataResponse> update(User user, @PathVariable("venueId") Long venueId, @RequestBody VenueUpdateRequest request) {
        VenueDataResponse venueDataResponse = venueService.update(user, venueId, request);
        return DtoToWebMapper.toWebResponse(venueDataResponse);
    }

    @DeleteMapping(
            path = "/api/venues/{venueId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable("venueId") Long venueId) {
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

    @DeleteMapping(
            path = "/api/venues/{venueId}/delete",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteReview(@PathVariable("venueId") Long id) {
        venueService.deleteVenueAdmin(id);
        return DtoToWebMapper.toWebResponse("Successfully deleted venue");
    }

    @PatchMapping(
            path = "/api/venues/{venueId}/rating",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> updateRating(@PathVariable("venueId") Long id,@RequestBody double rating) {
        venueService.updateRating(id, rating);
        return DtoToWebMapper.toWebResponse("Successfully updated rating");
    }

    @PatchMapping(
            path = "/api/venues/{venueId}/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<VenueDataResponse> updateAdmin(User user, @PathVariable("venueId") Long venueId, @RequestBody VenueUpdateRequest request) {
        VenueDataResponse venueDataResponse = venueService.updateAdmin(venueId, request);
        return DtoToWebMapper.toWebResponse(venueDataResponse);
    }
}
