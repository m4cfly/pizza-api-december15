package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.OrderLineDTO;
import dat.dtos.PizzaDTO;
import dat.dtos.OrderDTO;
import dat.dtos.PizzaUserDTO;
import dat.entities.OrderLine;
import dat.entities.Pizza;
import dat.entities.Order;
import dat.exceptions.ApiException;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Set;

public class OrderLineDAO implements IDAO<OrderLineDTO, Integer> {

    private static OrderLineDAO instance;
    private static EntityManagerFactory emf;

    public static OrderLineDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new OrderLineDAO();
        }
        return instance;
    }

    @Override
    public OrderLineDTO read(Integer orderLineId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            OrderLine orderLine = em.find(OrderLine.class, orderLineId);
            if (orderLine == null) {
                throw new ApiException(404, "OrderLine not found");
            }
            return new OrderLineDTO(orderLine);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during read");
        }
    }

    @Override
    public List<OrderLineDTO> readAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<OrderLineDTO> query = em.createQuery("SELECT new dat.dtos.OrderLineDTO(ol) FROM OrderLine ol", OrderLineDTO.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during readAll");
        }
    }

    @Override
    public OrderLineDTO create(OrderLineDTO orderLineDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            OrderLine orderLine = new OrderLine(orderLineDTO);
            em.persist(orderLine);
            em.getTransaction().commit();

            return new OrderLineDTO(orderLine);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during create");
        }
    }

    @Override
    public OrderLineDTO update(Integer orderLineId, OrderLineDTO orderLineDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            OrderLine orderLine = em.find(OrderLine.class, orderLineId);
            if (orderLine == null) {
                throw new ApiException(404, "OrderLine not found");
            }

            orderLine.setQuantity(orderLineDTO.getQuantity());
            orderLine.setPrice(orderLineDTO.getPrice());
            orderLine.setPizza(convertToPizza(orderLineDTO.getPizza()));
            orderLine.setOrder(convertToOrder(orderLineDTO.getOrder()));

            em.getTransaction().commit();

            return new OrderLineDTO(orderLine);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during update");
        }
    }

    @Override
    public void delete(Integer orderLineId, UserDTO userDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            OrderLine orderLine = em.find(OrderLine.class, orderLineId);
            if (orderLine == null) {
                throw new ApiException(404, "OrderLine not found");
            }

            em.remove(orderLine);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during delete");
        }
    }

    public List<OrderLineDTO> readByOrder(Integer orderId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<OrderLineDTO> query = em.createQuery("SELECT new dat.dtos.OrderLineDTO(ol) FROM OrderLine ol WHERE ol.order.id = :orderId", OrderLineDTO.class);
            query.setParameter("orderId", orderId);
            return query.getResultList();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during readByOrder");
        }
    }

    public OrderLineDTO[] populate() throws ApiException {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1);
        orderDTO.setOrderPrice(100.0);
        orderDTO.setOrderDate("2021-01-01");
        orderDTO.setUser(new PizzaUserDTO("user", Set.of("USER"))); // Corrected to use Set.of

        PizzaDTO pizzaDTO1 = new PizzaDTO(1, "Geo", "Delicious pizza", "Cheese, Tomato", 9.99, Pizza.PizzaType.REGULAR,null);
        PizzaDTO pizzaDTO2 = new PizzaDTO(2, "Margherita", "Classic pizza", "Cheese, Tomato, Basil", 8.99, Pizza.PizzaType.FAMILY, null);

        OrderLineDTO ol1 = new OrderLineDTO();
        ol1.setQuantity(2);
        ol1.setPrice(19.99);
        ol1.setPizza(pizzaDTO1);
        ol1.setOrder(orderDTO);

        OrderLineDTO ol2 = new OrderLineDTO();
        ol2.setQuantity(1);
        ol2.setPrice(14.99);
        ol2.setPizza(pizzaDTO2);
        ol2.setOrder(orderDTO);

        create(ol1);
        create(ol2);

        return new OrderLineDTO[]{ol1, ol2};
    }

    private Pizza convertToPizza(PizzaDTO pizzaDTO) {
        return new Pizza(pizzaDTO);
    }

    private Order convertToOrder(OrderDTO orderDTO) {
        return new Order(orderDTO);
    }

    public List<OrderLineDTO> readAllFromUser(UserDTO user) throws ApiException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<OrderLineDTO> query = em.createQuery("SELECT new dat.dtos.OrderLineDTO(ol) FROM OrderLine ol WHERE ol.user.id = :userId", OrderLineDTO.class);
            query.setParameter("userId", user.getUsername());
            return query.getResultList();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during readAllFromUser: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}