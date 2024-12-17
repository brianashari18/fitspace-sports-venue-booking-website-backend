package fitspace.fitspace_sports_venue_booking_website_backend.service;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.field.FieldDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.photo.PhotoDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.venue.VenueAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.venue.VenueDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.venue.VenueUpdateRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Photo;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Venue;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.EntityToDtoMapper;
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
        List<Venue> venues = venueRepository.findAll();

        if (venues.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No venue has been added yet");
        }

        return venues.stream()
                .map(EntityToDtoMapper::toVenueDataResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VenueDataResponse> getAllFromOwner(User user) {
        List<Venue> venues = venueRepository.findAllByOwner(user);

        if (venues.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No venues found for the specified owner");
        }

        return venues.stream()
                .map(EntityToDtoMapper::toVenueDataResponse)
                .toList();
    }
}
