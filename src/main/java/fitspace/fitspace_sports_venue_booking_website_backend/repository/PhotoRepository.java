package fitspace.fitspace_sports_venue_booking_website_backend.repository;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.Field;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findByPhotoUrl(String photoUrl);
    List<Photo> findByPhotoUrlIn(Set<String> photoUrls);

    void deleteAllByField(Field field);
}
