package fitspace.fitspace_sports_venue_booking_website_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private int id;

    @Column(unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String password;

    private String avatar;

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
}

