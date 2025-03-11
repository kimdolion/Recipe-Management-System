package recipes;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

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
        if (!userService.isUserRegistered(appUser.getEmail())) {
            userService.registerUser(appUser.getEmail(), appUser.getPassword());
            return ResponseEntity.ok("AppUser registered successfully");
        } else {
            return ResponseEntity.badRequest().body("AppUser already exists");
        }
    }

    @PostMapping("/recipe/new")
    public ResponseEntity<String> postRecipe(@RequestBody Recipe recipe, @AuthenticationPrincipal AppUser user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User must be logged in");
        }

        recipe.setAuthor(user);
        recipeService.addRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body("Recipe created successfully");
    }

    @Transactional
    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User must be logged in");
        }
        recipeService.deleteRecipe(id, String.valueOf(user.getEmail()));
        return ResponseEntity.ok("Recipe deleted");
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<String> updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe recipe, @AuthenticationPrincipal AppUser user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User must be logged in");
        }
        recipeService.updateRecipe(id, String.valueOf(user.getEmail()), recipe);
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

    @ExceptionHandler(RecipeService.UnauthorizedActionException.class)
    public ResponseEntity<String> handleUnauthorizedAction(RecipeService.UnauthorizedActionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

}
