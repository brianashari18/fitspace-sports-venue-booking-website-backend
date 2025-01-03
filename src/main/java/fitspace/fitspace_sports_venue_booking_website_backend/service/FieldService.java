package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldUpdateRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.*;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.EntityToDtoMapper;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FieldService {

    private static final Logger log = LoggerFactory.getLogger(FieldService.class);

    @Value("${photo.upload-dir}")
    private String uploadDir;

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


    public FieldDataResponse add(User user, FieldAddRequest request, List<MultipartFile> files, Long venueId) {
        validationService.validate(request);

        // Validasi dan pencarian Venue
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue is not found"));

        if (!Objects.equals(venue.getOwner().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner of this venue");
        }

        // Buat entitas Field
        Field newField = new Field();
        newField.setPrice(request.getPrice());
        newField.setType(request.getType());
        newField.setVenue(venue);

        // Simpan Field terlebih dahulu untuk mendapatkan ID
        Field finalNewField = fieldRepository.save(newField);

        // Simpan daftar foto ke Gallery setelah Field memiliki ID
        List<Photo> gallery = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = saveFileLocally(file);
            String photoUrl = "/uploads/" + fileName;

            Photo photo = new Photo();
            photo.setPhotoUrl(photoUrl);
            photo.setField(finalNewField); // Set Field yang sudah memiliki ID

            gallery.add(photo);
        }
        photoRepository.saveAll(gallery);

        List<Schedule> schedulesThisWeek = scheduleRepository.findSchedulesForWeek(
                LocalDate.now().with(java.time.DayOfWeek.MONDAY),
                LocalDate.now().with(java.time.DayOfWeek.SUNDAY)
        );
        if (schedulesThisWeek.isEmpty()) {
            createScheduleIfNotExist();
            schedulesThisWeek = scheduleRepository.findSchedulesForWeek(
                    LocalDate.now().with(java.time.DayOfWeek.MONDAY),
                    LocalDate.now().with(java.time.DayOfWeek.SUNDAY)
            );
        }

        List<FieldSchedule> fieldSchedules = schedulesThisWeek.stream()
                .map(schedule -> {
                    FieldSchedule fieldSchedule = new FieldSchedule();
                    fieldSchedule.setField(finalNewField);
                    fieldSchedule.setSchedule(schedule);
                    fieldSchedule.setStatus("Available");
                    return fieldSchedule;
                }).toList();
        fieldScheduleRepository.saveAll(fieldSchedules);

        finalNewField.setGallery(gallery);
        finalNewField.setFieldSchedules(fieldSchedules);

        return EntityToDtoMapper.toFieldDataResponse(finalNewField);
    }


    private String saveFileLocally(MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replace(" ", "_");


            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Path path = Paths.get(uploadDir, fileName);
            Files.write(path, file.getBytes());

            return fileName;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save file", e);
        }
    }

    public FieldDataResponse get(User user, Long venueId, Long fieldId) {

        List<Schedule> schedulesThisWeek = scheduleRepository.findSchedulesForWeek(
                LocalDate.now().with(java.time.DayOfWeek.MONDAY),
                LocalDate.now().with(java.time.DayOfWeek.SUNDAY)
        );

        if (schedulesThisWeek.isEmpty()) {
            createScheduleIfNotExist();
        }

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

    public FieldDataResponse update(User user, FieldUpdateRequest request, List<MultipartFile> files, Long venueId, Long fieldId) {
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

        if (request.getRemovedImages() != null && !request.getRemovedImages().isEmpty()) {
            List<Photo> photosToRemove = photoRepository.findAllByPhotoUrlIn(request.getRemovedImages());
            photoRepository.deleteAll(photosToRemove);

            // Hapus file dari sistem
            photosToRemove.forEach(photo -> {
                Path path = Paths.get(uploadDir, photo.getPhotoUrl().replace("/uploads/", ""));
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    log.error("Failed to delete file: " + path, e);
                }
            });
        }

        if (files != null && !files.isEmpty()) {
            List<Photo> newPhotos = files.stream().map(file -> {
                String fileName = saveFileLocally(file);
                Photo photo = new Photo();
                photo.setPhotoUrl("/uploads/" + fileName);
                photo.setField(field);
                return photo;
            }).toList();
            photoRepository.saveAll(newPhotos);
            field.getGallery().addAll(newPhotos);
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

    public List<FieldDataResponse> getAllField() {
        List<Field> fields = fieldRepository.findAll();
        return fields.stream().map(EntityToDtoMapper::toFieldDataResponse).toList();
    }

}
