package fitspace.fitspace_sports_venue_booking_website_backend.repository;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.Field;
import fitspace.fitspace_sports_venue_booking_website_backend.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findByPhotoUrl(String photoUrl);
    List<Photo> findByPhotoUrlIn(Set<String> photoUrls);
    List<Photo> findAllByPhotoUrlIn(List<String> photoUrls);

    void deleteAllByField(Field field);
}
