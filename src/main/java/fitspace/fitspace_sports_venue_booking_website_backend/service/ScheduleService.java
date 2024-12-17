package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Field;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Schedule;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.EntityToDtoMapper;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.FieldRepository;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private FieldRepository fieldRepository;

    private EntityToDtoMapper entityToDtoMapper = new EntityToDtoMapper();

    @Transactional
    public ScheduleDataResponse add(Integer fieldId, ScheduleAddRequest request) {
        fieldRepository.findById(fieldId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Field not found"));

        Schedule schedule = new Schedule();
        schedule.setDate(request.getDate());
        schedule.setTimeSlot(request.getTimeSlot());

        scheduleRepository.save(schedule);
        return entityToDtoMapper.toScheduleDataResponse(schedule);
    }

    @Transactional
    public void delete(Integer fieldId, Integer id) {
        fieldRepository.findById(fieldId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Field not found"));
        Schedule schedule = scheduleRepository.findById(id).
                orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        scheduleRepository.delete(schedule);
    }
}
