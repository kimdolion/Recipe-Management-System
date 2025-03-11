package recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RECIPES")
public class Recipe {
    @ManyToOne
    @JoinColumn(name = "user_email")
    private AppUser appUser;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    @JsonIgnore
    private Long id;

    @NotBlank
    @Column(name = "NAME")
    private String name;

    @NotBlank
    @Column(name = "DESCRIPTION")
    private String description;

    @NotBlank
    @Column(name = "CATEGORY")
    private String category;

    @NotEmpty
    @Column(name = "INGREDIENTS")
    private List<String> ingredients;

    @NotEmpty
    @Column(name = "DIRECTIONS")
    private List<String> directions;

    @Column(name= "DATE")
    private LocalDateTime date;

    private String ownerEmail;

    public Recipe() {
        this.ingredients = new ArrayList<>();
        this.directions = new ArrayList<>();
        this.date = LocalDateTime.now();
    }


    public Recipe(String name, String category, String description, List<String> ingredients, List<String> directions) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
        this.date = LocalDateTime.now();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(List<String> directions) {
        this.directions = directions;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}
