package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.OrderLineDAO;
import dat.dtos.OrderLineDTO;
import dat.exceptions.ApiException;
import dk.bugelhartmann.UserDTO;
import dat.dtos.PizzaUserDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class OrderLineController implements IController<OrderLineDTO, Integer> {
    private final OrderLineDAO dao;

    public OrderLineController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = OrderLineDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            OrderLineDTO orderLineDTO = dao.read(id);
            ctx.res().setStatus(200);
            ctx.json(orderLineDTO, OrderLineDTO.class);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Missing required parameter: id");
        }
    }

    @Override
    public void readAll(Context ctx) throws ApiException {
        List<OrderLineDTO> orderLineDTOS = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(orderLineDTOS, OrderLineDTO.class);
    }

    public void readAllFromUser(Context ctx) throws ApiException {
        UserDTO user = ctx.attribute("user");
        List<OrderLineDTO> orderLineDTOS = dao.readAllFromUser(user);
        ctx.res().setStatus(200);
        ctx.json(orderLineDTOS, OrderLineDTO.class);
    }

    @Override
    public void create(Context ctx) throws ApiException {
        OrderLineDTO orderLineDTO = ctx.bodyAsClass(OrderLineDTO.class);
        UserDTO user = ctx.attribute("user");
        PizzaUserDTO pizzaUserDTO = new PizzaUserDTO(user.getUsername(), user.getRoles());
        orderLineDTO.setUser(pizzaUserDTO);
        orderLineDTO = dao.create(orderLineDTO);
        ctx.res().setStatus(201);
        ctx.json(orderLineDTO, OrderLineDTO.class);
    }

    @Override
    public void update(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            OrderLineDTO orderLineDTOfromJson = ctx.bodyAsClass(OrderLineDTO.class);
            OrderLineDTO orderLineDTO = dao.update(id, orderLineDTOfromJson);
            ctx.res().setStatus(200);
            ctx.json(orderLineDTO, OrderLineDTO.class);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Missing required parameter: id");
        }
    }

    @Override
    public void delete(Context ctx) throws ApiException {
        try {
            UserDTO userDTO = ctx.attribute("user");
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.delete(id, userDTO);
            ctx.res().setStatus(204);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Missing required parameter: id");
        }
    }

    public void populate(Context ctx) throws ApiException {
        try {
            OrderLineDTO[] orderLineDTOS = dao.populate();
            ctx.res().setStatus(200);
            ctx.json("{ \"message\": \"Database has been populated with order lines\" }");
        } catch (PersistenceException e) {
            throw new ApiException(400, "Populator went wrong, dude");
        }
    }
}