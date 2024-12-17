package fitspace.fitspace_sports_venue_booking_website_backend.helper;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.bookings.BookingDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.fieldschedule.FieldScheduleDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.venue.VenueDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EntityToDtoMapper {

    public static FieldDataResponse toFieldDataResponse(Field field) {
        return FieldDataResponse.builder()
                .id(field.getId())
                .price(field.getPrice())
                .type(field.getType())
                .createdAt(field.getCreatedAt())
                .updatedAt(field.getUpdatedAt())
                .venueId(field.getVenue().getId())
                .reviews(field.getReviews() != null ? field.getReviews().stream().map(EntityToDtoMapper::toReviewDataResponse).collect(Collectors.toList()) : new ArrayList<>())
                .gallery(field.getGallery().stream().map(EntityToDtoMapper::toPhotoDataResponse).collect(Collectors.toList()))
                .fieldSchedules(field.getFieldSchedules().stream().map(EntityToDtoMapper::toFieldScheduleDataResponse).collect(Collectors.toList()))
                .build();
    }

    public static FieldScheduleDataResponse toFieldScheduleDataResponse(FieldSchedule fieldSchedule) {
        return FieldScheduleDataResponse.builder()
                .id(fieldSchedule.getId())
                .status(fieldSchedule.getStatus())
                .fieldId(fieldSchedule.getField().getId())
                .schedule(EntityToDtoMapper.toScheduleDataResponse(fieldSchedule.getSchedule()))
                .build();
    }

    public static ScheduleDataResponse toScheduleDataResponse(Schedule schedule) {
        return ScheduleDataResponse.builder()
                .id(schedule.getId())
                .date(schedule.getDate())
                .timeSlot(schedule.getTimeSlot())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }

    public static ReviewDataResponse toReviewDataResponse(Review review) {
        return ReviewDataResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .fieldId(review.getField().getId())
                .userId(review.getUser().getId())
                .build();
    }

    public static PhotoDataResponse toPhotoDataResponse(Photo photo) {
        return PhotoDataResponse.builder()
                .id(photo.getId())
                .photoUrl(photo.getPhotoUrl())
                .createdAt(photo.getCreatedAt())
                .updatedAt(photo.getUpdatedAt())
                .fieldId(photo.getField().getId())
                .build();
    }

    public static VenueDataResponse toVenueDataResponse(Venue venue) {
        return VenueDataResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .phoneNumber(venue.getPhoneNumber())
                .street(venue.getStreet())
                .district(venue.getDistrict())
                .cityOrRegency(venue.getCityOrRegency())
                .province(venue.getProvince())
                .postalCode(venue.getPostalCode())
                .latitude(venue.getLatitude())
                .longitude(venue.getLongitude())
                .rating(venue.getRating())
                .ownerId(venue.getOwner().getId())
                .fields(venue.getFields().stream().map(EntityToDtoMapper::toFieldDataResponse).collect(Collectors.toList()))
                .build();
    }

    public static BookingDataResponse toBookingDataResponse(Booking booking) {
        return BookingDataResponse.builder()
                .id(booking.getId())
                .customerId(booking.getCustomer().getId())
                .scheduleId(booking.getSchedule().getId())
                .status(booking.getStatus())
                .build();
    }
}
