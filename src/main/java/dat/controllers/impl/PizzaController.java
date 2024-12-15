package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.PizzaDAO;
import dat.dtos.PizzaDTO;
import dat.exceptions.ApiException;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class PizzaController implements IController<PizzaDTO, Integer> {
    private final PizzaDAO dao;

    public PizzaController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = PizzaDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            PizzaDTO pizzaDTO = dao.read(id);
            ctx.res().setStatus(200);
            ctx.json(pizzaDTO, PizzaDTO.class);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Missing required parameter: id");
        }
    }

    @Override
    public void readAll(Context ctx) throws ApiException {
        List<PizzaDTO> pizzaDTOS = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(pizzaDTOS, PizzaDTO.class);
    }

    public void readAllFromUser(Context ctx) throws ApiException {
        UserDTO user = ctx.attribute("user");
        List<PizzaDTO> pizzaDTOS = dao.readAllFromUser(user);
        ctx.res().setStatus(200);
        ctx.json(pizzaDTOS, PizzaDTO.class);
    }

    @Override
    public void create(Context ctx) throws ApiException {
        PizzaDTO pizzaDTO = ctx.bodyAsClass(PizzaDTO.class);
        UserDTO user = ctx.attribute("user");
        pizzaDTO.setUser(user);
        pizzaDTO = dao.create(pizzaDTO);
        ctx.res().setStatus(201);
        ctx.json(pizzaDTO, PizzaDTO.class);
    }

    @Override
    public void update(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            PizzaDTO pizzaDTOfromJson = ctx.bodyAsClass(PizzaDTO.class);
            PizzaDTO pizzaDTO = dao.update(id, pizzaDTOfromJson);
            ctx.res().setStatus(200);
            ctx.json(pizzaDTO, PizzaDTO.class);
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
            PizzaDTO[] pizzaDTOS = dao.populate();
            ctx.res().setStatus(200);
            ctx.json("{ \"message\": \"Database has been populated with pizzas\" }");
        } catch (PersistenceException e) {
            throw new ApiException(400, "Populator went wrong, dude");
        }
    }
}