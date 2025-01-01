package fitspace.fitspace_sports_venue_booking_website_backend.repository;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Venue> findFirstByOwnerAndId(User owner, Long id);
    List<Venue> findAllByOwner(User owner);

    @Query("SELECT v FROM Venue v WHERE v.fields IS NOT EMPTY")
    List<Venue> findAllWithFields();
}
