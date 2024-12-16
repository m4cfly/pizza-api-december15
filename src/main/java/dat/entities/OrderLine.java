package dat.entities;

import dat.dtos.OrderLineDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Setter
@Table(name = "order_lines")
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_id", nullable = false, unique = true)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_id")
    private Pizza pizza;  // Associated pizza item

    @Setter
    @Column(name = "quantity", nullable = false)
    private Integer quantity;  // Quantity of the pizza ordered

    @Setter
    @Column(name = "price", nullable = false)
    private Double price;  // Price of the pizza for this order line

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;  // Associated order

    public OrderLine(OrderLineDTO orderLineDTO) {
        this.id = orderLineDTO.getId();
        this.quantity = orderLineDTO.getQuantity();
        this.price = orderLineDTO.getPrice();

        // Handle pizza mapping
        if (orderLineDTO.getPizza() != null) {
            this.pizza = new Pizza(orderLineDTO.getPizza());
        }
    }
}
