package recipes;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipesByName(String name) {
        return new ArrayList<>(recipeRepository.findByNameContainingIgnoreCase(name));
    }

    public List<Recipe> getRecipesByCategory(String category) {
        return new ArrayList<>(recipeRepository.findByCategoryIgnoreCase(category));
    }

    public List<Recipe> getRecipesByAuthor(String email) {
        return recipeRepository.findByAuthorEmailIgnoreCase(email);
    }

    public void deleteRecipe(Long recipeId, String email) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeController.RecipeNotFoundException(recipeId));
        if (!recipe.getAuthor().getEmail().equals(email)) {
            throw new UnauthorizedActionException("You are not the author of this recipe.");
        }
        recipeRepository.delete(recipe);
    }

    public void updateRecipe(Long recipeId, String email, Recipe updatedRecipe) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeController.RecipeNotFoundException(recipeId));
        if (!recipe.getAuthor().getEmail().equals(email)) {
            throw new UnauthorizedActionException("You are not the author of this recipe.");
        }
        recipe.setName(updatedRecipe.getName());
        recipe.setIngredients(updatedRecipe.getIngredients());
        recipe.setDirections(updatedRecipe.getDirections());
        recipeRepository.save(recipe);
    }

    public class UnauthorizedActionException extends RuntimeException {
        public UnauthorizedActionException(String message) {
            super(message);
        }
    }

}
