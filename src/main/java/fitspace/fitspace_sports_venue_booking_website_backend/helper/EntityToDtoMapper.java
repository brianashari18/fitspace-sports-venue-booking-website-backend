package fitspace.fitspace_sports_venue_booking_website_backend.helper;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.review.ReviewDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.venue.VenueDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Field;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Photo;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Review;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Schedule;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Venue;

import java.util.stream.Collectors;

public class EntityToDtoMapper {

    public FieldDataResponse toFieldDataResponse(Field field) {
        return FieldDataResponse.builder()
                .id(field.getId())
                .price(field.getPrice())
                .type(field.getType())
                .createdAt(field.getCreatedAt())
                .updatedAt(field.getUpdatedAt())
                .venueId(field.getVenue().getId())
                .reviews(field.getReviews().stream().map(this::toReviewDataResponse).collect(Collectors.toList()))
                .gallery(field.getGallery().stream().map(this::toPhotoDataResponse).collect(Collectors.toList()))
                .schedules(field.getSchedules().stream().map(this::toScheduleDataResponse).collect(Collectors.toList()))
                .build();
    }

    public ScheduleDataResponse toScheduleDataResponse(Schedule schedule) {
        return ScheduleDataResponse.builder()
                .id(schedule.getId())
                .date(schedule.getDate())
                .timeSlot(schedule.getTimeSlot())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .bookingId(schedule.getBooking() != null ? schedule.getBooking().getId() : null)
                .build();
    }

    public ReviewDataResponse toReviewDataResponse(Review review) {
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

    public PhotoDataResponse toPhotoDataResponse(Photo photo) {
        return PhotoDataResponse.builder()
                .id(photo.getId())
                .photoUrl(photo.getPhotoUrl())
                .description(photo.getDescription())
                .createdAt(photo.getCreatedAt())
                .updatedAt(photo.getUpdatedAt())
                .fieldId(photo.getField().getId())
                .build();
    }

    public VenueDataResponse toVenueDataResponse(Venue venue) {
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
                .fields(venue.getFields().stream().map(this::toFieldDataResponse).collect(Collectors.toList()))
                .build();
    }
}
