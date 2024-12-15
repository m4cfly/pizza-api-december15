package dat.dtos;

import dat.entities.Order;
import dat.entities.OrderLine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderLineDTO {
    private Integer id;
    private int quantity;
    private double price;
    private PizzaDTO pizza;
    private OrderDTO order;

    public OrderLineDTO(Integer id, int quantity, double price, PizzaDTO pizza, OrderDTO order) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.pizza = pizza;
        this.order = order;
    }

    public OrderLineDTO(OrderLine orderLine) {
        this.id = orderLine.getId();
        this.quantity = orderLine.getQuantity();
        this.price = orderLine.getPrice();
        this.pizza = new PizzaDTO(orderLine.getPizza());
        this.order = new OrderDTO(orderLine.getOrder());
    }

    // Getters and setters

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }
}
