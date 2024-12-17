package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings.BookingDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings.BookingUpdateRequest;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<BookingDataResponse> get(User user) {
        var book =  bookingRepository.findAllByCustomer(user).stream().map(booking ->
                 BookingDataResponse.builder()
                         .status(booking.getStatus())
                         .customerId(booking.getCustomer().getId())
                         .scheduleId(booking.getSchedule().getId())
                         .build()).toList();
        return book;
    }

    public void delete(User user, int bookingId) {
        var book = bookingRepository.findByCustomerAndId(user, bookingId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));
        bookingRepository.delete(book);
    }

    public BookingDataResponse update(User user, int bookingId, BookingUpdateRequest bookingRequest) {
        var book = bookingRepository.findByCustomerAndId(user, bookingId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));

        book.setStatus(bookingRequest.getStatus());
        bookingRepository.save(book);
        return BookingDataResponse.builder()
                .status(book.getStatus())
                .customerId(book.getCustomer().getId())
                .scheduleId(book.getSchedule().getId())
                .build();
    }

}
