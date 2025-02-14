package recipes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecipeController {
    private Map<Integer, Recipe> recipesList = new HashMap<>();

    @PostMapping(path = "/recipe/new")
    public ResponseEntity<Map<String, Integer>> postRecipe(@RequestBody Recipe recipe) {
        Integer newId = recipesList.size() + 1;
        recipesList.put(newId, recipe);

        Map<String, Integer> response = new HashMap<>();
        response.put("id", newId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = "/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable int id) {
        if (recipesList.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(recipesList.get(id));
        } else {
            throw new RecipeNotFoundException();
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public static class RecipeNotFoundException extends RuntimeException {
    }
}
