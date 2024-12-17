package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Field;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Review;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.EntityToDtoMapper;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.FieldRepository;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Transactional
    public ReviewDataResponse create(Integer fieldId, User user, ReviewAddRequest request) {
        Review review = new Review();
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Field not found"));;
        review.setComment(request.getComment());
        review.setField(field);
        review.setUser(user);
        review.setRating(request.getRating());
        reviewRepository.save(review);

        return fitspace.fitspace_sports_venue_booking_website_backend.helper.EntityToDtoMapper.toReviewDataResponse(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewDataResponse> getAllReviews(Integer fieldId) {
        List<Review> reviews = reviewRepository.findAllByField_Id(fieldId);

        if(reviews == null || reviews.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }

        return reviews.stream()
                .map(fitspace.fitspace_sports_venue_booking_website_backend.helper.EntityToDtoMapper::toReviewDataResponse)
                .toList();
    }

    @Transactional
    public void delete(Integer reviewId, Integer fieldId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        reviewRepository.delete(review);
    }

}
