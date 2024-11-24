package fitspace.fitspace_sports_venue_booking_website_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    private int id;

    private LocalDate date;

    @Column(name = "time_slot")
    private String timeSlot;

    private Long price;

    private String status = "available";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "field_id", referencedColumnName = "id")
    private Field field;
}
