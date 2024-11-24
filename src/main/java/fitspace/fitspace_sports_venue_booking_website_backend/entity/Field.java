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
@Table(name = "fields")
public class Field {

    @Id
    private int id;

    private String type;

    @Column(name = "floor_type")
    private String floorType;

    @Column(name = "court_length")
    private double courtLength;

    @Column(name = "court_width")
    private double courtWidth;

    @Column(name = "net_height")
    private double netHeight;

    @Column(name = "goal_width")
    private double goalWidth;

    @Column(name = "goal_height")
    private double goalHeight;

    @Column(name = "ring_height")
    private double ringHeight;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "venue_id", referencedColumnName = "id")
    private Venue venue;

    @OneToMany(mappedBy = "field")
    private List<Schedule> schedules;
}
