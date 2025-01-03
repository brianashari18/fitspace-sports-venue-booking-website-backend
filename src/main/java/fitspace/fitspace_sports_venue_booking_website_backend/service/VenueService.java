package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.venue.VenueAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.venue.VenueDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.venue.VenueUpdateRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.FieldSchedule;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Photo;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Venue;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.EntityToDtoMapper;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.FieldScheduleRepository;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.VenueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.beans.PropertyDescriptor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueService {

    private static final Logger log = LoggerFactory.getLogger(VenueService.class);
    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private FieldScheduleRepository fieldScheduleRepository;

    private void updateFieldScheduleStatus() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now(); // Waktu sekarang

        // Cari semua FieldSchedule dengan tanggal sebelum hari ini atau tanggal hari ini dengan timeSlot yang sudah lewat
        List<FieldSchedule> pastSchedules = fieldScheduleRepository.findAll().stream()
                .filter(fieldSchedule -> {
                    LocalDate scheduleDate = fieldSchedule.getSchedule().getDate();
                    String timeSlot = fieldSchedule.getSchedule().getTimeSlot();

                    // Jika tanggal sebelum hari ini, maka langsung true
                    if (scheduleDate.isBefore(today)) {
                        return true;
                    }

                    // Jika tanggal hari ini, periksa apakah timeSlot sudah lewat
                    if (scheduleDate.isEqual(today)) {
                        return isTimeSlotPassed(timeSlot, now);
                    }

                    return false; // Selain itu, tidak perlu diperbarui
                })
                .collect(Collectors.toList());

        // Perbarui status menjadi "Not Available"
        pastSchedules.forEach(schedule -> schedule.setStatus("Not Available"));

        log.info("Updated FieldSchedules: {}", pastSchedules);

        // Simpan kembali data yang diperbarui
        fieldScheduleRepository.saveAll(pastSchedules);
    }

    // Metode untuk memeriksa apakah timeSlot sudah lewat
    private boolean isTimeSlotPassed(String timeSlot, LocalDateTime now) {
        // Pecah timeSlot menjadi waktu mulai dan waktu akhir, misalnya "06:00 - 07:00"
        String[] parts = timeSlot.split(" - ");
        if (parts.length != 2) {
            log.warn("Invalid timeSlot format: {}", timeSlot);
            return false; // Abaikan jika format salah
        }

        try {
            // Parse waktu mulai
            LocalTime startTime = LocalTime.parse(parts[0].trim());

            // Gabungkan waktu mulai dengan tanggal hari ini
            LocalDateTime slotStartDateTime = LocalDateTime.of(now.toLocalDate(), startTime);

            // Periksa apakah waktu sekarang sudah melewati waktu mulai
            return now.isAfter(slotStartDateTime);
        } catch (Exception e) {
            log.error("Error parsing timeSlot: {}", timeSlot, e);
            return false; // Abaikan jika parsing gagal
        }
    }


    @Transactional
    public VenueDataResponse create(User user, VenueAddRequest request) {
        validationService.validate(request);

        Venue venue = new Venue();
        venue.setName(request.getName());
        venue.setPhoneNumber(request.getPhoneNumber());
        venue.setStreet(request.getStreet());
        venue.setDistrict(request.getDistrict());
        venue.setCityOrRegency(request.getCityOrRegency());
        venue.setProvince(request.getProvince());
        venue.setPostalCode(request.getPostalCode());
        venue.setLatitude(request.getLatitude());
        venue.setLongitude(request.getLongitude());
        venue.setOwner(user);

        venueRepository.save(venue);

        return EntityToDtoMapper.toVenueDataResponse(venue);
    }


    @Transactional(readOnly = true)
    public VenueDataResponse get(Long id) {
        updateFieldScheduleStatus();

        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));

        System.out.println(venue.getName());
        return EntityToDtoMapper.toVenueDataResponse(venue);
    }

    @Transactional
    public VenueDataResponse update(User user, Long id, VenueUpdateRequest request) {
        validationService.validate(request);

        Venue venue = venueRepository.findFirstByOwnerAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));

        BeanUtils.copyProperties(request, venue, getNullPropertyNames(request));

        venueRepository.save(venue);
        return EntityToDtoMapper.toVenueDataResponse(venue);
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapperImpl wrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = wrapper.getPropertyDescriptors();
        List<String> nullPropertyNames = new ArrayList<>();
        for (PropertyDescriptor pd : pds) {
            if (wrapper.getPropertyValue(pd.getName()) == null) {
                nullPropertyNames.add(pd.getName());
            }
        }
        return nullPropertyNames.toArray(new String[0]);
    }


    @Transactional
    public void delete(User user, Long id) {
        Venue venue = venueRepository.findFirstByOwnerAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));

        venueRepository.delete(venue);
    }

    @Transactional(readOnly = true)
    public List<VenueDataResponse> getAll() {
        updateFieldScheduleStatus();

        List<Venue> venues = venueRepository.findAllWithFields();

        return venues.stream()
                .map(EntityToDtoMapper::toVenueDataResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VenueDataResponse> getAllFromOwner(User user) {
        List<Venue> venues = venueRepository.findAllByOwner(user);

        return venues.stream()
                .map(EntityToDtoMapper::toVenueDataResponse)
                .toList();
    }

    @Transactional
    public void deleteVenueAdmin(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));

        venueRepository.delete(venue);
    }
@Transactional
    public void updateRating(long id , double rating) {
    Venue venue = venueRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));
    venue.setRating(rating);
    venueRepository.save(venue);
    }

    @Transactional
    public VenueDataResponse updateAdmin(Long id, VenueUpdateRequest request) {
        validationService.validate(request);

        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));

        BeanUtils.copyProperties(request, venue, getNullPropertyNames(request));

        venueRepository.save(venue);
        return EntityToDtoMapper.toVenueDataResponse(venue);
    }
}

