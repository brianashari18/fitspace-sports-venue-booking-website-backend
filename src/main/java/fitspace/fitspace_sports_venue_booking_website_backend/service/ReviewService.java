package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewUpdateRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Field;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Review;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.EntityToDtoMapper;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.FieldRepository;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private ValidationService validationService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FieldRepository fieldRepository;


    @Transactional
    public ReviewDataResponse create(Integer fieldId, User user, ReviewAddRequest request) {
        validationService.validate(request);

        Review review = new Review();
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Field not found"));;
        review.setComment(request.getComment());
        review.setField(field);
        review.setUser(user);
        review.setRating(request.getRating());

        try {
            reviewRepository.save(review);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving review");
        }

        return EntityToDtoMapper.toReviewDataResponse(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewDataResponse> getAllReviews(Integer fieldId) {
        List<Review> reviews = reviewRepository.findAllByField_Id(fieldId);

        if(reviews == null || reviews.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }

        return reviews.stream()
                .map(EntityToDtoMapper::toReviewDataResponse)
                .toList();
    }

    @Transactional
    public void delete(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewDataResponse> getAlls() {
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream()
                .map(EntityToDtoMapper::toReviewDataResponse)
                .toList();
    }

    @Transactional
    public ReviewDataResponse update(long review_id, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(review_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        if(request.getComment() != null) {
            review.setComment(request.getComment());
        }

        if(request.getRating() != null) {
            review.setRating(request.getRating());
        }

        reviewRepository.save(review);

        return EntityToDtoMapper.toReviewDataResponse(review);
    }

}
