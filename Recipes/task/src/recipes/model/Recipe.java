package recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @NotBlank(message = "Name is mandatory!")
    private String name;

    @NotBlank(message = "Category is mandatory!")
    private String category;

    @NotBlank(message = "Description is mandatory!")
    private String description;

    @ElementCollection
    @NotEmpty(message = "We need to have some ingredients!")
    @Size(min = 1, message = "Minimal ingredients number is 1")
    private List <String> ingredients;

    @ElementCollection
    @NotEmpty(message = "We need to have directions in order to make a dish!")
    @Size(min = 1, message = "Minimal directions number is 1")
    private List <String> directions;

    @UpdateTimestamp
    private LocalDateTime date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id") // naziv spoljnog kljuca kog Hibernate generise unutar "Recipe" entiteta
    private User user;

}
