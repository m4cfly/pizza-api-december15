package dat.entities;

import dat.dtos.OrderDTO;
import dat.dtos.OrderLineDTO;
import dat.dtos.PizzaUserDTO;
import dat.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Setter
@ToString
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Column(name = "order_price", nullable = false)
    private Double orderPrice;

    @Setter
    @Column(name = "order_date", nullable = false)
    private String orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // Associated User who made the order

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_id")
    private Pizza pizza;  // Associated Pizza for the order

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrderLine> orderLines = new HashSet<>();  // List of order lines (pizza and quantity)

    public Order(OrderDTO orderDTO) {
        this.id = orderDTO.getId();
        this.orderPrice = orderDTO.getOrderPrice();
        this.orderDate = orderDTO.getOrderDate();

        // Handle user if exists
        if (orderDTO.getUser() != null) {
            this.user = new User(convertToUserDTO(orderDTO.getUser())); // Convert PizzaUserDTO to UserDTO
        }

        // Map order lines from the DTO
        if (orderDTO.getOrderLines() != null) {
            this.orderLines = new HashSet<>();
            for (OrderLineDTO orderLineDTO : orderDTO.getOrderLines()) {
                this.orderLines.add(new OrderLine(orderLineDTO));
            }
        }
    }

    private UserDTO convertToUserDTO(PizzaUserDTO pizzaUserDTO) {
        // Conversion logic here
        return new UserDTO(pizzaUserDTO.getUsername(), pizzaUserDTO.getRoles());
    }

    public void addOrderLine(OrderLine orderLine) {
        orderLines.add(orderLine);
        orderLine.setOrder(this);  // Set the order for this line
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(getOrderPrice(), order.getOrderPrice()) &&
                Objects.equals(getOrderDate(), order.getOrderDate()) &&
                Objects.equals(getUser(), order.getUser()) &&
                Objects.equals(getPizza(), order.getPizza()) &&
                Objects.equals(getOrderLines(), order.getOrderLines());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderPrice(), getOrderDate(), getUser(), getPizza(), getOrderLines());
    }
}