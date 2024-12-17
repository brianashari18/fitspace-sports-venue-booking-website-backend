package fitspace.fitspace_sports_venue_booking_website_backend.repository;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long> {
    Optional<Field> findById(Integer id);
}