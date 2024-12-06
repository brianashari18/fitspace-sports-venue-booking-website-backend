package fitspace.fitspace_sports_venue_booking_website_backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")

public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "venue_id")
    private int venueId;

    private String facility;
    private LocalDate date;

    @Column(name = "time_slot")
    private String timeSlot;
    private double price;
    private Timestamp timestamp;


}
