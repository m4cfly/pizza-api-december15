package dat.populator;

import dat.security.entities.User;
import dat.security.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Set;
import java.util.stream.Collectors;

public class DatabasePopulator {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");

    public static void main(String[] args) {
        populateDatabase();
    }

    public static void populateDatabase() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Create User
            User user = new User();
            user.setUsername("user");
            user.setPassword("test123");
            em.persist(user);

            // Convert User to OrderUser
            Set<String> roles = user.getRoles().stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toSet());
            OrderUser orderUser = new OrderUser(user.getUsername(), roles);
            em.persist(orderUser);

            // Create Pizzas
            Pizza pizza1 = new Pizza();
            pizza1.setName("Geo");
            pizza1.setDescription("Delicious pizza");
            pizza1.setToppings("Cheese, Tomato");
            pizza1.setPrice(9.99);
            pizza1.setPizzaType(Pizza.PizzaType.REGULAR);
            pizza1.setUser(user); // Set User directly
            em.persist(pizza1);

            Pizza pizza2 = new Pizza();
            pizza2.setName("Margherita");
            pizza2.setDescription("Classic pizza");
            pizza2.setToppings("Cheese, Tomato, Basil");
            pizza2.setPrice(8.99);
            pizza2.setPizzaType(Pizza.PizzaType.FAMILY);
            pizza2.setUser(user); // Set User directly
            em.persist(pizza2);

            // Create Order
            Order order = new Order();
            order.setOrderPrice(25.99);
            order.setOrderDate("2023-10-01");
            order.setUser(orderUser);
            em.persist(order);

            // Create OrderLines
            OrderLine orderLine1 = new OrderLine();
            orderLine1.setQuantity(2);
            orderLine1.setPrice(19.98);
            orderLine1.setPizza(pizza1);
            orderLine1.setOrder(order);
            orderLine1.setUser(user); // Set User directly
            em.persist(orderLine1);

            OrderLine orderLine2 = new OrderLine();
            orderLine2.setQuantity(1);
            orderLine2.setPrice(8.99);
            orderLine2.setPizza(pizza2);
            orderLine2.setOrder(order);
            orderLine2.setUser(user); // Set User directly
            em.persist(orderLine2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}