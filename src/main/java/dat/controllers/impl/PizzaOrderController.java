package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.PizzaOrderDAO;
import dat.dtos.PizzaOrderDTO;
import dat.exceptions.ApiException;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class PizzaOrderController implements IController<PizzaOrderDTO, Integer> {
    private final PizzaOrderDAO dao;

    public PizzaOrderController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = PizzaOrderDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            PizzaOrderDTO pizzaOrderDTO = dao.read(id);
            ctx.res().setStatus(200);
            ctx.json(pizzaOrderDTO, PizzaOrderDTO.class);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Missing required parameter: id");
        }
    }

    @Override
    public void readAll(Context ctx) throws ApiException {
        List<PizzaOrderDTO> pizzaOrderDTOS = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(pizzaOrderDTOS, PizzaOrderDTO.class);
    }

    public void readAllFromUser(Context ctx) throws ApiException {
        try {
            UserDTO user = ctx.attribute("user");
            List<PizzaOrderDTO> pizzaOrderDTOS = dao.readAllFromUser(user);
            ctx.res().setStatus(200);
            ctx.json(pizzaOrderDTOS, PizzaOrderDTO.class);
        } catch (Exception e) {
            throw new ApiException(400, "Something went wrong during readAllFromUser");
        }
    }

    @Override
    public void create(Context ctx) throws ApiException {
        PizzaOrderDTO pizzaOrderDTO = ctx.bodyAsClass(PizzaOrderDTO.class);
        UserDTO user = ctx.attribute("user");
        pizzaOrderDTO.setUser(user);
        pizzaOrderDTO = dao.create(pizzaOrderDTO);
        ctx.res().setStatus(201);
        ctx.json(pizzaOrderDTO, PizzaOrderDTO.class);
    }

    @Override
    public void update(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            PizzaOrderDTO pizzaOrderDTOfromJson = ctx.bodyAsClass(PizzaOrderDTO.class);
            PizzaOrderDTO pizzaOrderDTO = dao.update(id, pizzaOrderDTOfromJson);
            ctx.res().setStatus(200);
            ctx.json(pizzaOrderDTO, PizzaOrderDTO.class);
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
            PizzaOrderDTO[] pizzaOrderDTOS = dao.populate();
            ctx.res().setStatus(200);
            ctx.json("{ \"message\": \"Database has been populated with pizza orders\" }");
        } catch (PersistenceException e) {
            throw new ApiException(400, "Populator went wrong, dude");
        }
    }
}