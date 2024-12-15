package dat.entities;

import dat.dtos.PizzaDTO;
import dat.security.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Setter
@ToString
@Table(name = "pizza")
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pizza_id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "description")
    private String description;

    @Setter
    @Column(name = "toppings")
    private String toppings;

    @Setter
    @Column(name = "price", nullable = false)
    private Double price;

    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orderLines;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "pizza_type", nullable = false)
    private PizzaType pizzaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Pizza(PizzaDTO pizzaDTO) {
        this.id = pizzaDTO.getPizzaId();
        this.name = pizzaDTO.getName();
        this.description = pizzaDTO.getDescription();
        this.toppings = pizzaDTO.getToppings();
        this.price = pizzaDTO.getPrice();
        this.pizzaType = pizzaDTO.getPizzaType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pizza pizza)) return false;
        return Objects.equals(getName(), pizza.getName()) &&
                Objects.equals(getDescription(), pizza.getDescription()) &&
                Objects.equals(getToppings(), pizza.getToppings()) &&
                Objects.equals(getPrice(), pizza.getPrice()) &&
                getPizzaType() == pizza.getPizzaType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getToppings(), getPrice(), getPizzaType());
    }

    public enum PizzaType {
        CHILDSIZE, FAMILY, PARTY, REGULAR;
    }
}