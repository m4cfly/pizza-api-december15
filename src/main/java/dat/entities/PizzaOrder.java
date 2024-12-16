package dat.entities;

import dat.dtos.PizzaOrderDTO;
import dat.security.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class PizzaOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String pizzaName;
    private int quantity;
    private double price;

    @ToString.Exclude   // Avoid recursion
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user = null;

    public PizzaOrder(PizzaOrderDTO pizzaOrderDTO) {
        this.id = pizzaOrderDTO.getId();
        this.pizzaName = pizzaOrderDTO.getPizzaName();
        this.quantity = pizzaOrderDTO.getQuantity();
        this.price = pizzaOrderDTO.getPrice();
        this.user = new User(pizzaOrderDTO.getUser());
    }

    public void removeUser() {
        if (this.user != null) {
            this.user.getPizzaOrders().remove(this);
        }
        this.user = null;
    }
}