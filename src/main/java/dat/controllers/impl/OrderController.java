package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.OrderDAO;
import dat.dtos.OrderDTO;
import dat.dtos.PizzaUserDTO;
import dat.exceptions.ApiException;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class OrderController implements IController<OrderDTO, Integer> {
    private final OrderDAO dao;

    public OrderController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = OrderDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            OrderDTO orderDTO = dao.read(id);
            ctx.res().setStatus(200);
            ctx.json(orderDTO, OrderDTO.class);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Missing required parameter: id");
        }
    }

    @Override
    public void readAll(Context ctx) throws ApiException {
        List<OrderDTO> orderDTOS = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(orderDTOS, OrderDTO.class);
    }

    @Override
    public void create(Context ctx) throws ApiException {
        OrderDTO orderDTO = ctx.bodyAsClass(OrderDTO.class);
        UserDTO user = ctx.attribute("user");
        PizzaUserDTO pizzaUserDTO = new PizzaUserDTO(user.getUsername(), user.getRoles());
        orderDTO.setUser(pizzaUserDTO);
        orderDTO = dao.create(orderDTO);
        ctx.res().setStatus(201);
        ctx.json(orderDTO, OrderDTO.class);
    }

    @Override
    public void update(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            OrderDTO orderDTOfromJson = ctx.bodyAsClass(OrderDTO.class);
            OrderDTO orderDTO = dao.update(id, orderDTOfromJson);
            ctx.res().setStatus(200);
            ctx.json(orderDTO, OrderDTO.class);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Missing required parameter: id");
        }
    }

    @Override
    public void delete(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.delete(id);
            ctx.res().setStatus(204);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Missing required parameter: id");
        }
    }

    public void populate(Context ctx) throws ApiException {
        dao.populate();
        ctx.res().setStatus(200);
        ctx.result("Database populated with sample data");
    }

    public void readAllFromUser(Context ctx) throws ApiException {
        UserDTO user = ctx.attribute("user");
        if (user == null) {
            throw new ApiException(400, "User not found in context");
        }
        List<OrderDTO> orderDTOS = dao.readAllFromUser(user.getUsername());
        ctx.res().setStatus(200);
        ctx.json(orderDTOS, OrderDTO.class);
    }
}