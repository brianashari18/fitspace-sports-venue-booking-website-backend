package fitspace.fitspace_sports_venue_booking_website_backend.repository;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
}
