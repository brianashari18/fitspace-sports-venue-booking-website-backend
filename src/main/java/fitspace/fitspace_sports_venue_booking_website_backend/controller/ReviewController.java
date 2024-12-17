package fitspace.fitspace_sports_venue_booking_website_backend.controller;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.WebResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings.BookingAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping(
            path = "/api/{fieldId}/reviews",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ReviewDataResponse> create(@PathVariable("fieldId") Integer fieldId , User user, @RequestBody ReviewAddRequest request){
        ReviewDataResponse reviewDataResponse = reviewService.create(fieldId,user,request);
        return WebResponse.<ReviewDataResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(reviewDataResponse)
                .build();
    }

    @DeleteMapping(
            path = "/api/{fieldId}/reviews/{reviewId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(@PathVariable("reviewId") Integer reviewId, @PathVariable Integer fieldId){
        reviewService.delete(reviewId,fieldId);
        return WebResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data("Review Deleted Successfully")
                .build();
    }

    @GetMapping(
            path = "/api/{fieldId}/reviews",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ReviewDataResponse>> getAllReviews(@PathVariable("fieldId") Integer fieldId){
        List<ReviewDataResponse> reviewDataResponses = reviewService.getAllReviews(fieldId);
        return WebResponse.<List<ReviewDataResponse>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(reviewDataResponses)
                .build();
    }
}
