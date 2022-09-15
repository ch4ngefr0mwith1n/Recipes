package recipes.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.Recipe;
import recipes.repository.RecipesRepository;

@Service
public class RecipeService {

    private RecipesRepository recipesRepository;

    @Autowired
    public RecipeService(RecipesRepository recipesRepository) {
        this.recipesRepository = recipesRepository;
    }

    public Recipe save(Recipe recipe) {
        return recipesRepository.save(recipe);
    }

    public Optional<Recipe> findByID(Long id) {
        return recipesRepository.findById(id);
    }

    public List<Recipe> findAllByCategory(String category) {
        return recipesRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> findAllByName(String name) {
        return recipesRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    public void delete(Long id, String username) {
        Optional<Recipe> optRecipe = findByID(id);
        // u slučaju da recept ne postoji:
        if (optRecipe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // u slučaju da korisnik nema ovlašćenja:
        if (!optRecipe.get().getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        recipesRepository.deleteById(id);
    }

    public Recipe update(Recipe recipe, String username) {
        Optional<Recipe> optRecipe = findByID(recipe.getId());
        // u slučaju da recept ne postoji:
        if (optRecipe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // u slučaju da korisnik nema ovlašćenja:
        if (!optRecipe.get().getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // stavljanje korisnika za autora trenutnog recepta:
        recipe.setUser(optRecipe.get().getUser());
        // metoda "save" čuva i vraća trenutni recept:
        save(recipe);

        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

}
