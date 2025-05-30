package recipes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByCategoryIgnoreCase(String category);

    List<Recipe> findByNameContainingIgnoreCase(String name);

    List<Recipe> findByAuthorEmailIgnoreCase(String email);
}

