package fitspace.fitspace_sports_venue_booking_website_backend.controller;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.WebResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings.BookingAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewUpdateRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping(
            path = "/api/fields/{fieldId}/reviews",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ReviewDataResponse> create(@PathVariable("fieldId") Integer fieldId , User user, @RequestBody ReviewAddRequest request){
        ReviewDataResponse reviewDataResponse = reviewService.create(fieldId,user,request);

        log.info("DATA: {}", reviewDataResponse);

        return WebResponse.<ReviewDataResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(reviewDataResponse)
                .build();
    }

    @DeleteMapping(
            path = "/api/reviews/{reviewId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(@PathVariable("reviewId") Long reviewId){
        reviewService.delete(reviewId);
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

    @GetMapping(
            path = "/api/reviews",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ReviewDataResponse>> getAlls(){
        List<ReviewDataResponse> reviewDataResponses = reviewService.getAlls();
        return WebResponse.<List<ReviewDataResponse>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(reviewDataResponses)
                .build();
    }

    @PatchMapping(
            path = "/api/reviews/{reviewId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ReviewDataResponse> update(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewUpdateRequest request){
        ReviewDataResponse review = reviewService.update(reviewId,request);
        return WebResponse.<ReviewDataResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(review)
                .build();
    }


}
