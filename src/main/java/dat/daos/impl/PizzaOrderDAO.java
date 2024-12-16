package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.PizzaOrderDTO;
import dat.entities.PizzaOrder;
import dat.exceptions.ApiException;
import dat.security.daos.SecurityPopulatorDAO;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PizzaOrderDAO implements IDAO<PizzaOrderDTO, Integer> {
    private static PizzaOrderDAO instance;
    private final EntityManagerFactory emf;

    private PizzaOrderDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static PizzaOrderDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new PizzaOrderDAO(emf);
        }
        return instance;
    }

    @Override
    public PizzaOrderDTO read(Integer id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            PizzaOrder pizzaOrder = em.find(PizzaOrder.class, id);
            if (pizzaOrder == null) {
                throw new ApiException(404, "PizzaOrder not found");
            }
            return new PizzaOrderDTO(pizzaOrder);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during read");
        }
    }

    @Override
    public List<PizzaOrderDTO> readAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<PizzaOrderDTO> query = em.createQuery("SELECT new dat.dtos.PizzaOrderDTO(p) FROM PizzaOrder p", PizzaOrderDTO.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during readAll");
        }
    }

    public List<PizzaOrderDTO> readAllFromUser(UserDTO user) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<PizzaOrderDTO> query = em.createQuery("SELECT new dat.dtos.PizzaOrderDTO(p) FROM PizzaOrder p WHERE p.user.username = :username", PizzaOrderDTO.class);
            query.setParameter("username", user.getUsername());
            return query.getResultList();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during readAllFromUser");
        }
    }

    @Override
    public PizzaOrderDTO create(PizzaOrderDTO pizzaOrderDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            PizzaOrder pizzaOrder = new PizzaOrder(pizzaOrderDTO);
            em.persist(pizzaOrder);
            em.getTransaction().commit();
            return new PizzaOrderDTO(pizzaOrder);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during create");
        }
    }

    @Override
    public PizzaOrderDTO update(Integer id, PizzaOrderDTO pizzaOrderDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            PizzaOrder pizzaOrder = em.find(PizzaOrder.class, id);
            if (pizzaOrder == null) {
                throw new ApiException(404, "PizzaOrder not found");
            }
            pizzaOrder.setPizzaName(pizzaOrderDTO.getPizzaName());
            pizzaOrder.setQuantity(pizzaOrderDTO.getQuantity());
            pizzaOrder.setPrice(pizzaOrderDTO.getPrice());
            em.getTransaction().commit();
            return new PizzaOrderDTO(pizzaOrder);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during update");
        }
    }

    @Override
    public void delete(Integer id, UserDTO userDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            PizzaOrder pizzaOrder = em.find(PizzaOrder.class, id);
            if (pizzaOrder == null) {
                throw new ApiException(404, "PizzaOrder not found");
            }
            if (!pizzaOrder.getUser().getUsername().equals(userDTO.getUsername())) {
                throw new ApiException(403, "You are not allowed to delete this pizza order");
            }
            em.remove(pizzaOrder);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during delete");
        }
    }

    public PizzaOrderDTO[] populate() throws ApiException {
        UserDTO[] users = SecurityPopulatorDAO.populateUsers(emf);
        UserDTO userDTO = users[0];
        UserDTO adminDTO = users[1];
        PizzaOrderDTO p1 = new PizzaOrderDTO(null, "Margherita", 2, 15.99, userDTO);
        PizzaOrderDTO p2 = new PizzaOrderDTO(null, "Pepperoni", 1, 18.99, userDTO);
        PizzaOrderDTO p3 = new PizzaOrderDTO(null, "Hawaiian", 3, 20.99, adminDTO);
        create(p1);
        create(p2);
        create(p3);
        return new PizzaOrderDTO[] {p1, p2, p3};
    }
}