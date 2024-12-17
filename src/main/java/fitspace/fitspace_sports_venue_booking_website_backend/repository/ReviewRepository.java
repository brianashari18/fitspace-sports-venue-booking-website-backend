package fitspace.fitspace_sports_venue_booking_website_backend.repository;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByField_Id(Integer fieldId);

    Optional<Review> findById(Integer Id);
}
