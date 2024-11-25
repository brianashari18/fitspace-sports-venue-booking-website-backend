package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.Photo;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Venue;
import fitspace.fitspace_sports_venue_booking_website_backend.model.*;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.PhotoRepository;
import fitspace.fitspace_sports_venue_booking_website_backend.repository.VenueRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

@Service
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ValidationService validationService;

    private VenueDataResponse toVenueDataResponse(Venue venue) {
        return VenueDataResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .phoneNumber(venue.getPhoneNumber())
                .street(venue.getStreet())
                .district(venue.getDistrict())
                .cityOrRegency(venue.getCityOrRegency())
                .province(venue.getProvince())
                .country(venue.getCountry())
                .postalCode(venue.getPostalCode())
                .latitude(venue.getLatitude())
                .longitude(venue.getLongitude())
                .rating(venue.getRating())
                .reviewsCount(venue.getReviewsCount())
                .ownerId(venue.getOwner().getId())
                .gallery(venue.getGallery().stream().map(photo -> {
                    return PhotoDataResponse.builder()
                            .id(photo.getId())
                            .photoUrl(photo.getPhotoUrl())
                            .description(photo.getDescription())
                            .venueId(photo.getVenue().getId())
                            .build();
                }).toList())
                .build();
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
        venue.setCountry(request.getCountry());
        venue.setPostalCode(request.getPostalCode());
        venue.setLatitude(request.getLatitude());
        venue.setLongitude(request.getLongitude());
        venue.setOwner(user);

        List<Photo> gallery = request.getPhotoAddRequests().stream().map(photoAddRequest -> {
            validationService.validate(photoAddRequest);
            Photo photo = new Photo();
            photo.setPhotoUrl(photoAddRequest.getPhotoUrl());
            photo.setDescription(photoAddRequest.getDescription());
            photo.setVenue(venue);
            return photo;
        }).toList();

        venue.setGallery(gallery);
        venueRepository.save(venue);

        return toVenueDataResponse(venue);
    }


    @Transactional(readOnly = true)
    public VenueDataResponse get(Integer id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));

        return toVenueDataResponse(venue);
    }

    @Transactional
    public VenueDataResponse update(User user, Integer id, VenueUpdateRequest request) {
        validationService.validate(request);

        Venue venue = venueRepository.findFirstByOwnerAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));

        BeanUtils.copyProperties(request, venue, getNullPropertyNames(request));

        venueRepository.save(venue);
        return toVenueDataResponse(venue);
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
    public void delete(User user, Integer id) {
        Venue venue = venueRepository.findFirstByOwnerAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found"));

        venueRepository.delete(venue);
    }

    @Transactional(readOnly = true)
    public List<VenueDataResponse> getAll() {
        List<Venue> venues = venueRepository.findAll();

        if (venues.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No venue has been added yet");
        }

        return venues.stream()
                .map(this::toVenueDataResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VenueDataResponse> getAllFromOwner(User user) {
        List<Venue> venues = venueRepository.findAllByOwner(user);

        if (venues.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No venues found for the specified owner");
        }

        return venues.stream()
                .map(this::toVenueDataResponse)
                .toList();
    }
}
