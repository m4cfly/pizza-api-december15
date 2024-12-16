package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.PizzaDTO;
import dat.entities.Pizza;
import dat.exceptions.ApiException;
import dat.security.daos.SecurityPopulatorDAO;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PizzaDAO implements IDAO<PizzaDTO, Integer> {

    private static PizzaDAO instance;
    private static EntityManagerFactory emf;

    public static PizzaDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PizzaDAO();
        }
        return instance;
    }

    @Override
    public PizzaDTO read(Integer pizzaId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Pizza pizza = em.find(Pizza.class, pizzaId);
            if (pizza == null) {
                throw new ApiException(404, "Pizza not found");
            }
            return new PizzaDTO(pizza);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during read");
        }
    }

    @Override
    public List<PizzaDTO> readAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<PizzaDTO> query = em.createQuery("SELECT new dat.dtos.PizzaDTO(p) FROM Pizza p", PizzaDTO.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during readAll");
        }
    }

    public List<PizzaDTO> readAllFromUser(UserDTO user) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<PizzaDTO> query = em.createQuery("SELECT new dat.dtos.PizzaDTO(p) FROM Pizza p WHERE p.user.id = :userId", PizzaDTO.class);
            query.setParameter("userId", user.getUsername());
            return query.getResultList();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during readAllFromUser");
        }
    }

    @Override
    public PizzaDTO create(PizzaDTO pizzaDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Pizza pizza = new Pizza(pizzaDTO);
            em.persist(pizza);
            em.getTransaction().commit();
            return new PizzaDTO(pizza);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during create");
        }
    }

    @Override
    public PizzaDTO update(Integer pizzaId, PizzaDTO pizzaDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Pizza pizza = em.find(Pizza.class, pizzaId);
            if (pizza == null) {
                throw new ApiException(404, "Pizza not found");
            }

            pizza.setName(pizzaDTO.getName());
            pizza.setDescription(pizzaDTO.getDescription());
            pizza.setToppings(pizzaDTO.getToppings());
            pizza.setPrice(pizzaDTO.getPrice());
            pizza.setPizzaType(pizzaDTO.getPizzaType());
            em.getTransaction().commit();
            return new PizzaDTO(pizza);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during update");
        }
    }

    @Override
    public void delete(Integer pizzaId, UserDTO userDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Pizza pizza = em.find(Pizza.class, pizzaId);
            if (pizza == null) {
                throw new ApiException(404, "Pizza not found");
            }
            em.remove(pizza);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during delete");
        }
    }

    public PizzaDTO[] populate() throws ApiException {
        UserDTO[] users = SecurityPopulatorDAO.populateUsers(emf);
        UserDTO userDTO = users[0];
        UserDTO adminDTO = users[1];
        PizzaDTO pizza1 = new PizzaDTO(null, "Margherita", "Classic margherita pizza", "Tomato, mozzarella, basil", 9.99, Pizza.PizzaType.REGULAR, userDTO);
        PizzaDTO pizza2 = new PizzaDTO(null, "Pepperoni", "Classic pepperoni pizza", "Tomato, mozzarella, pepperoni", 11.99, Pizza.PizzaType.REGULAR, userDTO);
        PizzaDTO pizza3 = new PizzaDTO(null, "Hawaiian", "Classic Hawaiian pizza", "Tomato, mozzarella, ham, pineapple", 12.99, Pizza.PizzaType.REGULAR, adminDTO);
        create(pizza1);
        create(pizza2);
        create(pizza3);
        return new PizzaDTO[]{pizza1, pizza2, pizza3};
    }
}