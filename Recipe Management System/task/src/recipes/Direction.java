package recipes;

import jakarta.persistence.*;

@Entity
@Table(name = "DIRECTIONS")
public class Direction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment ID
    private Long id;

    @Column(name = "directions", nullable = false)
    private String directions;

    // Constructors
    public Direction() {}

    public Direction(String directions) {
        this.directions = directions;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String ingredients) {
        this.directions = ingredients;
    }
}
