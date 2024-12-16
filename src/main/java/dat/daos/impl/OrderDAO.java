
package dat.daos.impl;

import dat.dtos.OrderDTO;
import dat.dtos.PizzaUserDTO;
import dat.entities.Order;
import dat.exceptions.ApiException;
import dat.security.daos.SecurityPopulatorDAO;
import dat.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private static OrderDAO instance;
    private static EntityManagerFactory emf;

    public static OrderDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new OrderDAO();
        }
        return instance;
    }

    public OrderDTO read(Integer orderId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Order order = em.find(Order.class, orderId);
            if (order == null) {
                throw new ApiException(404, "Order not found");
            }

            // Ensure user is fetched
            order.getUser();
            return new OrderDTO(order);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during read");
        }
    }

    public List<OrderDTO> readAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<OrderDTO> query = em.createQuery("SELECT new dat.dtos.OrderDTO(o) FROM Order o", OrderDTO.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during readAll");
        }
    }

    public OrderDTO create(OrderDTO orderDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create a new order entity from the OrderDTO
            Order order = new Order(orderDTO);

            // Set the user if provided
            if (orderDTO.getUser() != null) {
                order.setUser(convertToUser(orderDTO.getUser()));
            }

            // Persist the order entity
            em.persist(order);
            em.getTransaction().commit();

            // Return the newly created OrderDTO
            return new OrderDTO(order);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during create");
        }
    }

    public OrderDTO update(Integer orderId, OrderDTO orderDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the existing order entity by its ID
            Order order = em.find(Order.class, orderId);
            if (order == null) {
                throw new ApiException(404, "Order not found");
            }

            // Update the existing order entity with the new data from the OrderDTO
            order.setOrderPrice(orderDTO.getOrderPrice());
            order.setOrderDate(orderDTO.getOrderDate());
            if (orderDTO.getUser() != null) {
                order.setUser(convertToUser(orderDTO.getUser())); // Convert PizzaUserDTO to User
            }

            // Commit the transaction
            em.getTransaction().commit();

            // Return the updated OrderDTO
            return new OrderDTO(order);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during update");
        }
    }

    public void delete(Integer orderId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the order entity by its ID
            Order order = em.find(Order.class, orderId);
            if (order == null) {
                throw new ApiException(404, "Order not found");
            }

            // Remove the order entity from the database
            em.remove(order);

            // Commit the transaction
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during delete");
        }
    }

    public void populate() throws ApiException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Create sample users
            UserDTO[] users = SecurityPopulatorDAO.populateUsers(emf);
            UserDTO userDTO = users[0];
            UserDTO adminDTO = users[1];

            // Create sample orders
            List<Order> sampleOrders = new ArrayList<>();
            sampleOrders.add(new Order("2021-01-01", 100.0, new User(userDTO)));
            sampleOrders.add(new Order("2021-01-02", 200.0, new User(userDTO)));
            sampleOrders.add(new Order("2021-01-03", 300.0, new User(adminDTO)));

            // Persist sample orders
            for (Order order : sampleOrders) {
                em.persist(order);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new ApiException(400, "Something went wrong during populate: " + e.getMessage());
        } finally {
            em.close();
        }
    }


    public List<OrderDTO> readAllFromUser(String username) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<OrderDTO> query = em.createQuery(
                    "SELECT new dat.dtos.OrderDTO(o) FROM Order o WHERE o.user.username = :username", OrderDTO.class);
            query.setParameter("username", username);
            return query.getResultList();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during readAllFromUser");
        }
    }

    // Method to convert PizzaUserDTO to User
    private User convertToUser(PizzaUserDTO pizzaUserDTO) {
        return new User(new UserDTO(pizzaUserDTO.getUsername(), pizzaUserDTO.getRoles()));
    }
}