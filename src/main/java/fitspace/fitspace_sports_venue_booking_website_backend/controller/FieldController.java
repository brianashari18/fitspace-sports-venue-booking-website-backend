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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @PostMapping(
            path = "/api/venues/{venueId}/fields",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<FieldDataResponse> add(
            User user,
            @PathVariable("venueId") Long venueId,
            @RequestPart("field") FieldAddRequest fieldAddRequest,
            @RequestPart("files") List<MultipartFile> files
    ) {
        FieldDataResponse fieldDataResponse = fieldService.add(user, fieldAddRequest, files, venueId);
        return DtoToWebMapper.toWebResponse(fieldDataResponse);
    }


    @GetMapping(
            path = "/api/venues/{venueId}/fields/{fieldId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<FieldDataResponse> get(User user, @PathVariable("venueId") Long venueId, @PathVariable("fieldId") Long fieldId) {
        FieldDataResponse fieldDataResponse = fieldService.get(user, venueId, fieldId);
        return DtoToWebMapper.toWebResponse(fieldDataResponse);
    }

    @GetMapping(
            path = "/api/venues/{venueId}/fields",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<FieldDataResponse>> get(User user, @PathVariable("venueId") Long venueId) {
        List<FieldDataResponse> fieldDataResponses = fieldService.getAll(user, venueId);
        return DtoToWebMapper.toWebResponse(fieldDataResponses);
    }

    @PatchMapping(
            path = "/api/venues/{venueId}/fields/{fieldId}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public WebResponse<FieldDataResponse> update(
            User user,
            @PathVariable("venueId") Long venueId,
            @PathVariable("fieldId") Long fieldId,
            @RequestPart("field") FieldUpdateRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        FieldDataResponse fieldDataResponse = fieldService.update(user, request, files, venueId, fieldId);
        return DtoToWebMapper.toWebResponse(fieldDataResponse);
    }


    @DeleteMapping(
            path = "/api/venues/{venueId}/fields/{fieldId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable("venueId") Long venueId, @PathVariable("fieldId") Long fieldId) {
        fieldService.delete(user, venueId, fieldId);
        return DtoToWebMapper.toWebResponse("Successfully deleted field");
    }

    @GetMapping(
            path = "/api/fields",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<FieldDataResponse>> getAllField() {
        List<FieldDataResponse> fieldDataResponses = fieldService.getAllField();
        return DtoToWebMapper.toWebResponse(fieldDataResponses);
    }


}
