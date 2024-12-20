package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings.BookingAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings.BookingDataResponse;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings.BookingUpdateStatusRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.*;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.EntityToDtoMapper;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FieldScheduleRepository fieldScheduleRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Transactional
    public BookingDataResponse create(User user, Long venueId,BookingAddRequest request){
        Booking booking = new Booking();
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));

        Field field = fieldRepository.findByVenueAndType(venue,request.getFieldName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Field not found"));

        Schedule schedule = scheduleRepository.findByDateAndTimeSlot(request.getDate(), request.getTimeSlot())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));

        FieldSchedule fs = fieldScheduleRepository.findByFieldAndSchedule(field, schedule)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FieldSchedule not found"));

        if(fs.getStatus().equals("not available")){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule is not available");
        }

        fs.setStatus("not available");
        fieldRepository.save(field);

        booking.setStatus("ongoing");
        booking.setCustomer(user);
        booking.setSchedule(schedule);
        bookingRepository.save(booking);

        return EntityToDtoMapper.toBookingDataResponse(booking);

    }

    @Transactional
    public List<BookingDataResponse> get(User user) {
        var book =  bookingRepository.findAllByCustomer(user).stream().map(booking ->
                 BookingDataResponse.builder()
                         .id(booking.getId())
                         .status(booking.getStatus())
                         .customerId(booking.getCustomer().getId())
                         .scheduleId(booking.getSchedule().getId())
                         .build()).toList();
        return book;
    }

    @Transactional
    public void delete(User user, Long bookingId) {
        var book = bookingRepository.findByCustomerAndId(user, bookingId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));
        bookingRepository.delete(book);
    }

    public BookingDataResponse updateStatus(User user, long bookingId, BookingUpdateStatusRequest request) {
        var book = bookingRepository.findByCustomerAndId(user,bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        book.setStatus(request.getStatus());
        return EntityToDtoMapper.toBookingDataResponse(bookingRepository.save(book));
    }

}
