package fitspace.fitspace_sports_venue_booking_website_backend.controller;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.WebResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldUpdateRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.DtoToWebMapper;
import fitspace.fitspace_sports_venue_booking_website_backend.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @PostMapping(
            path = "/api/venues/{venueId}/fields",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<FieldDataResponse> add(User user, @RequestBody FieldAddRequest request,
                                              @PathVariable("venueId") Long venueId) {
        FieldDataResponse fieldDataResponse = fieldService.add(request, venueId);
        return DtoToWebMapper.toWebResponse(fieldDataResponse);
    }

    @GetMapping(
            path = "/api/venues/{venueId}/fields/{fieldId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<FieldDataResponse> get(User user, @PathVariable("venueId") Long venueId, @PathVariable("fieldId") Long fieldId) {
        FieldDataResponse fieldDataResponse = fieldService.get(venueId, fieldId);
        return DtoToWebMapper.toWebResponse(fieldDataResponse);
    }

    @GetMapping(
            path = "/api/venues/{venueId}/fields",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<FieldDataResponse>> get(User user, @PathVariable("venueId") Long venueId) {
        List<FieldDataResponse> fieldDataResponses = fieldService.getAll(venueId);
        return DtoToWebMapper.toWebResponse(fieldDataResponses);
    }

    @PatchMapping(
            path = "/api/venues/fields/{fieldId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<FieldDataResponse> update(@RequestBody FieldUpdateRequest request, @PathVariable("fieldId") Long fieldId) {
        FieldDataResponse fieldDataResponse = fieldService.update(request, fieldId);
        return DtoToWebMapper.toWebResponse(fieldDataResponse);
    }
}
