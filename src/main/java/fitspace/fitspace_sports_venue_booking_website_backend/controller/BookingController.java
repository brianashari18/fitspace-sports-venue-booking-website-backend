package fitspace.fitspace_sports_venue_booking_website_backend.controller;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.WebResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings.BookingAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings.BookingDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings.BookingUpdateStatusRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.DtoToWebMapper;
import fitspace.fitspace_sports_venue_booking_website_backend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @PostMapping(
            path = "/api/{venueId}/bookings",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<BookingDataResponse> create(User user, @PathVariable Long venueId, @RequestBody BookingAddRequest request) {
        BookingDataResponse bookingDataResponse = bookingService.create(user, venueId, request);
        return DtoToWebMapper.toWebResponse(bookingDataResponse);
    }

    @GetMapping(
            path = "/api/bookings",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse <List<BookingDataResponse>> get(User user) {
        List<BookingDataResponse> bookings = bookingService.get(user);
        return WebResponse.<List<BookingDataResponse>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(bookings)
                .build();
    }

    @DeleteMapping(
            path = "/api/{bookingId}/bookings",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable Long bookingId) {
        bookingService.delete(user, bookingId);
        return DtoToWebMapper.toWebResponse("Delete Booking Successfully");
    }

    @PatchMapping(
            path = "/api/{bookingId}/bookings",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<BookingDataResponse> updateStatus(User user, @PathVariable Long bookingId, @RequestBody BookingUpdateStatusRequest request) {
        BookingDataResponse bookingDataResponse = bookingService.updateStatus(user,bookingId,request);
        return DtoToWebMapper.toWebResponse(bookingDataResponse);
    }
}
