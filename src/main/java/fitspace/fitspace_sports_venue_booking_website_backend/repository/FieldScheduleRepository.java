package fitspace.fitspace_sports_venue_booking_website_backend.repository;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.Field;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.FieldSchedule;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldScheduleRepository extends JpaRepository<FieldSchedule, Long> {

    List<FieldSchedule> findBySchedule(Schedule schedule);
    List<FieldSchedule> findAllByField(Field field);
    FieldSchedule findFirstByField(Field field);

}
