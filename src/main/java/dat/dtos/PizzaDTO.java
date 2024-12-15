package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.entities.Pizza;
import dat.security.entities.User;
import dk.bugelhartmann.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PizzaDTO {
    private Integer pizzaId;
    private String name;
    private String description;
    private String toppings;
    private Double price;
    private Pizza.PizzaType pizzaType;
    private UserDTO user = null;

    public PizzaDTO(Pizza pizza) {
        this.pizzaId = pizza.getId();
        this.name = pizza.getName();
        this.description = pizza.getDescription();
        this.toppings = pizza.getToppings();
        this.price = pizza.getPrice();
        this.pizzaType = pizza.getPizzaType();
        if (pizza.getUser() != null) {
            User userEntity = pizza.getUser();
            this.user = new UserDTO(userEntity.getUsername(), userEntity.getRolesAsStrings());
        }
    }
}