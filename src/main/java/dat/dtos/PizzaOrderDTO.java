package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.entities.PizzaOrder;
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
public class PizzaOrderDTO {
    private Integer id;
    private String pizzaName;
    private int quantity;
    private double price;
    private UserDTO user = null;

    public PizzaOrderDTO(PizzaOrder pizzaOrder) {
        this.id = pizzaOrder.getId();
        this.pizzaName = pizzaOrder.getPizzaName();
        this.quantity = pizzaOrder.getQuantity();
        this.price = pizzaOrder.getPrice();
        if (pizzaOrder.getUser() != null) {
            User userEntity = pizzaOrder.getUser();
            this.user = new UserDTO(userEntity.getUsername(), userEntity.getRolesAsStrings());
        }
    }
}