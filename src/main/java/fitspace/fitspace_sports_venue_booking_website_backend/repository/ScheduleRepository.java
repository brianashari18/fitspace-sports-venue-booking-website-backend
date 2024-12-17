package fitspace.fitspace_sports_venue_booking_website_backend.repository;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findById(Integer id);
}
