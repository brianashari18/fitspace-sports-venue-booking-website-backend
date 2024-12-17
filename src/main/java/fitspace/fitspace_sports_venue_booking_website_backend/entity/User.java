package fitspace.fitspace_sports_venue_booking_website_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String password;

    private String role = "user";

    @Column(unique = true)
    private String token;

    @Column(name = "token_expired_at")
    private LocalDateTime tokenExpiredAt;

    @Column(unique = true)
    private String otp;

    @Column(name = "otp_expired_at")
    private LocalDateTime otpExpiredAt;

    @Column(unique = true, name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expired_at")
    private LocalDateTime resetTokenExpiredAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "owner")
    private List<Venue> venues;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings;

}

