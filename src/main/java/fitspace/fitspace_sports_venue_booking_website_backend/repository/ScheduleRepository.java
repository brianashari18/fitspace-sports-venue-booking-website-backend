package fitspace.fitspace_sports_venue_booking_website_backend.repository;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s WHERE s.date BETWEEN :startDate AND :endDate")
    List<Schedule> findSchedulesForWeek(LocalDate startDate, LocalDate endDate);
    Schedule findByTimeSlotAndDate(String timeSlot, LocalDate date);
    Optional<Schedule> findById(Integer id);

    Optional<Schedule> findByDateAndTimeSlot(LocalDate date, String timeSlot);
}
