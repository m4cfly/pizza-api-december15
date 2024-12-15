package dat.dtos;

import dat.entities.Order;
import dat.entities.OrderLine;
import dat.security.entities.User;
import dk.bugelhartmann.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

    private Integer id;
    private Double orderPrice;
    private String orderDate;
    private PizzaUserDTO user;  // UserDTO for the user who placed the order
    private List<OrderLineDTO> orderLines;  // List of OrderLineDTOs

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.orderPrice = order.getOrderPrice();
        this.orderDate = order.getOrderDate();

        // Handle user mapping
        if (order.getUser() != null) {
            this.user = new PizzaUserDTO(order.getUser());
        }


        // Map order lines from the order.http entity
        if (order.getOrderLines() != null) {
            this.orderLines = order.getOrderLines().stream()
                    .map(orderLine -> new OrderLineDTO(orderLine))
                    .collect(Collectors.toList());
        }
    }
}
