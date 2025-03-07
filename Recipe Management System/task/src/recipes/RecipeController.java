package recipes;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, Long>> postRecipe(@Valid @RequestBody Recipe recipe) {
        Recipe newRecipe = recipeRepository.save(recipe);
        Map<String, Long> response = new HashMap<>();
        response.put("id", newRecipe.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable long id) {
        return recipeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException(id);
        }
        recipeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRecipe(@PathVariable long id, @Valid @RequestBody Recipe updatedRecipe) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipe.setName(updatedRecipe.getName());
                    recipe.setCategory(updatedRecipe.getCategory());
                    recipe.setDescription(updatedRecipe.getDescription());
                    recipe.setIngredients(updatedRecipe.getIngredients());
                    recipe.setDirections(updatedRecipe.getDirections());
                    recipeRepository.save(recipe);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @GetMapping("/search/")
    public ResponseEntity<List<Recipe>> searchRecipes(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name) throws BadRequestException {

        List<Recipe> recipes = List.of();

        if (category == null && name == null) {
            throw new BadRequestException();
        }
        if (category != null) {
            recipes = recipeRepository.findByCategoryIgnoreCase(category);
        }
        if (name != null) {
            recipes = recipeRepository.findByNameContainingIgnoreCase(name);
        }

        recipes.sort(Comparator.comparing(Recipe::getDate).reversed());
        return ResponseEntity.ok(recipes);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class RecipeNotFoundException extends RuntimeException {
        public RecipeNotFoundException(long id) {
            super("Recipe with ID " + id + " not found.");
        }
        public RecipeNotFoundException(String searchParam) {
            super("Recipe with search " + searchParam + " not found.");
        }
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<String> handleRecipeNotFoundException(RecipeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
