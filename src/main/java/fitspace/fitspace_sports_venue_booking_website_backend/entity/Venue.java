package fitspace.fitspace_sports_venue_booking_website_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String street;

    private String district;

    @Column(name = "city_or_regency")
    private String cityOrRegency;

    private String province;

    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    private double latitude;

    private double longitude;

    private double rating = 0.0;

    @Column(name = "reviews_count")
    private int reviewsCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Photo> gallery;

    @OneToMany(mappedBy = "venue")
    private List<Field> fields;

    @OneToMany(mappedBy = "venue")
    private List<Review> reviews;

    @OneToMany(mappedBy = "venue")
    private List<Booking> bookings;
}
