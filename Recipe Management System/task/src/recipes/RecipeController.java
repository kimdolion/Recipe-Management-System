package recipes;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecipeController {

    private final UserService userService;
    private final RecipeService recipeService;

    public RecipeController(UserService userService, RecipeService recipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
    }

    // Register appUser endpoint
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AppUser appUser) {
        if (!userService.isUserRegistered(appUser.getUsername())) {
            userService.registerUser(appUser.getUsername(), appUser.getPassword());
            return ResponseEntity.ok("AppUser registered successfully");
        } else {
            return ResponseEntity.badRequest().body("AppUser already exists");
        }
    }

    @PostMapping("/recipe/new")
    public ResponseEntity<Map<String, Long>> postRecipe(@Valid @RequestBody Recipe recipe) {
        String username = getAuthenticatedUsername();
        Recipe newRecipe = recipeService.addRecipe(username, recipe);
        Map<String, Long> response = new HashMap<>();
        response.put("id", newRecipe.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id, @RequestParam Long userId) {
        recipeService.deleteRecipe(id, String.valueOf(userId));
        return ResponseEntity.ok("Recipe deleted");
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<String> updateRecipe(@PathVariable Long id, @RequestParam Long userId, @Valid @RequestBody Recipe recipe) {
        recipeService.updateRecipe(id, String.valueOf(userId), recipe);
        return ResponseEntity.ok("Recipe updated");
    }

    @GetMapping("/recipe/search/")
    public ResponseEntity<List<Recipe>> searchRecipes(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name) throws BadRequestException {

        List<Recipe> recipes = List.of();

        if (category == null && name == null) {
            throw new BadRequestException();
        }
        if (category != null) {
            recipes = recipeService.getRecipesByCategory(category);
        }
        if (name != null) {
            recipes = recipeService.getRecipesByName(name);
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

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
