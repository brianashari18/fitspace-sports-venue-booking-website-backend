package fitspace.fitspace_sports_venue_booking_website_backend.repository;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.Booking;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByCustomer(User customer);

    Optional<Booking> findByCustomerAndId(User customer,Long id);
}
