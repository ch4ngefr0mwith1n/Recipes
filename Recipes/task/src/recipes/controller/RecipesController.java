package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.Recipe;
import recipes.service.RecipeService;
import recipes.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
public class RecipesController {

    private RecipeService recipeService;
    private UserService userService;

    @Autowired
    public RecipesController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @PostMapping("/api/recipe/new")
    public Map<String, Long> setRecipe(@Valid @RequestBody Recipe recipe, @AuthenticationPrincipal UserDetails details) {
        recipe.setUser(userService.findByEmail(details.getUsername()).get());
        recipe = recipeService.save(recipe);
        return Map.of("id", recipe.getId());
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipeByID(@PathVariable long id) {
        Optional<Recipe> optRecipe = recipeService.findByID(id);

        if (optRecipe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return optRecipe.get();
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> deleteRecipe(@PathVariable Long id, @AuthenticationPrincipal UserDetails details) {
        recipeService.delete(id, details.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/api/recipe/{id}")
    public void updateRecipe(@Valid @RequestBody Recipe recipe, @PathVariable Long id,
                             @AuthenticationPrincipal UserDetails details) {
        recipe.setId(id);
        recipeService.update(recipe, details.getUsername());
    }


    @GetMapping(value = "/api/recipe/search", params = "category")
    public List<Recipe> getRecipesByCategory(@RequestParam("category") String category) {
        return recipeService.findAllByCategory(category);
    }

    @GetMapping(value = "/api/recipe/search", params = "name")
    public List<Recipe> getRecipesByName(@RequestParam("name") String name) {
        return recipeService.findAllByName(name);
    }

}
