package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldUpdateRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.*;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.EntityToDtoMapper;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FieldService {

    private static final Logger log = LoggerFactory.getLogger(FieldService.class);
    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private ValidationService validationService;
    @Autowired
    private FieldScheduleRepository fieldScheduleRepository;

    public void createScheduleIfNotExist() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<Schedule> schedulesThisWeek = scheduleRepository.findSchedulesForWeek(startOfWeek, endOfWeek);

        if (schedulesThisWeek.isEmpty()) {
            for (int i = 0; i < 7; i++) {
                LocalDate scheduleDate = startOfWeek.plusDays(i);

                List<String> timeSlots = List.of(
                        "06:00 - 07:00", "07:00 - 08:00", "08:00 - 09:00", "09:00 - 10:00",
                        "10:00 - 11:00", "11:00 - 12:00", "12:00 - 13:00", "13:00 - 14:00",
                        "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00",
                        "18:00 - 19:00", "19:00 - 20:00", "20:00 - 21:00", "21:00 - 22:00",
                        "22:00 - 23:00", "23:00 - 00:00"
                );

                for (String timeSlot : timeSlots) {
                    Schedule newSchedule = new Schedule();
                    newSchedule.setDate(scheduleDate);
                    newSchedule.setTimeSlot(timeSlot);
                    newSchedule.setCreatedAt(LocalDateTime.now());
                    newSchedule.setUpdatedAt(LocalDateTime.now());

                    scheduleRepository.save(newSchedule);
                }
            }
        }
    }


    public FieldDataResponse add(User user, FieldAddRequest request, Long venueId) {
        validationService.validate(request);

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<Schedule> schedulesThisWeek = scheduleRepository.findSchedulesForWeek(startOfWeek, endOfWeek);
        if (schedulesThisWeek.isEmpty()) {
            createScheduleIfNotExist();
            schedulesThisWeek = scheduleRepository.findSchedulesForWeek(startOfWeek, endOfWeek);
        }

        Field newField = new Field();
        newField.setPrice(request.getPrice());
        newField.setType(request.getType());

        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue is not found"));

        if (!Objects.equals(venue.getOwner().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner of this venue");
        }

        List<Photo> gallery = request.getGallery().stream().map(photoAddRequest -> {
            Photo photo = new Photo();
            photo.setPhotoUrl(photoAddRequest.getPhotoUrl());
            photo.setField(newField);
            return photo;
        }).toList();

        List<FieldSchedule> fieldSchedules = schedulesThisWeek.stream().map(schedule -> {
            FieldSchedule fieldSchedule = new FieldSchedule();
            fieldSchedule.setField(newField);
            fieldSchedule.setSchedule(schedule);
            return fieldSchedule;
        }).toList();

        newField.setGallery(gallery);
        newField.setVenue(venue);
        newField.setFieldSchedules(fieldSchedules);

        fieldRepository.save(newField);

        return EntityToDtoMapper.toFieldDataResponse(newField);
    }

    public FieldDataResponse get(User user, Long venueId, Long fieldId) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue is not found"));

        if (!Objects.equals(venue.getOwner().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner of this venue");
        }

        Field field = fieldRepository.findFirstByIdAndVenue(fieldId, venue).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Field is not found"));
        return EntityToDtoMapper.toFieldDataResponse(field);
    }

    public List<FieldDataResponse> getAll(User user, Long venueId) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue is not found"));

        if (!Objects.equals(venue.getOwner().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner of this venue");
        }

        List<Field> fields = fieldRepository.findAllByVenue(venue);
        return fields.stream().map(EntityToDtoMapper::toFieldDataResponse).toList();
    }

    public FieldDataResponse update(User user, FieldUpdateRequest request, Long venueId, Long fieldId) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue is not found"));

        if (!Objects.equals(venue.getOwner().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner of this venue");
        }

        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Field not found"));

        List<FieldSchedule> existingFieldSchedules = field.getFieldSchedules();

        if (request.getFieldSchedules() != null && !request.getFieldSchedules().isEmpty()) {
            for (int i = 0; i < existingFieldSchedules.size(); i++) {
                FieldSchedule fieldSchedule = existingFieldSchedules.get(i);
                String newStatus = request.getFieldSchedules().get(i).getStatus();
                if (newStatus != null) {
                    fieldSchedule.setStatus(newStatus);
                }
            }
            fieldScheduleRepository.saveAll(existingFieldSchedules);
        }

        if (request.getGallery() != null && !request.getGallery().isEmpty()) {
            field.getGallery().clear(); // Clears the existing collection but keeps the same collection instance
            request.getGallery().stream().map(photoUpdateRequest -> {
                Photo photo = new Photo();
                photo.setPhotoUrl(photoUpdateRequest.getPhotoUrl());
                photo.setField(field);
                return photo;
            }).forEach(field.getGallery()::add);
        }

        if (request.getPrice() != null) {
            field.setPrice(request.getPrice());
        }

        if (request.getType() != null) {
            field.setType(request.getType());
        }


        fieldRepository.save(field);

        return EntityToDtoMapper.toFieldDataResponse(field);
    }

    public void delete(User user, Long venueId, Long fieldId) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue is not found"));

        if (!Objects.equals(venue.getOwner().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner of this venue");
        }

        Field field = fieldRepository.findFirstByIdAndVenue(fieldId, venue).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Field is not found"));

        fieldRepository.delete(field);
    }


}
