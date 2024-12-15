package dat.routes;

import dat.controllers.impl.PizzaController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class PizzaRoute {
    private final PizzaController pizzaController = new PizzaController();

    protected EndpointGroup getRoutes() {
        return () -> {
            post("/populate", pizzaController::populate, Role.ANYONE);
            post("/", pizzaController::create, Role.USER);
            get("/", pizzaController::readAll, Role.ANYONE);
            get("/user", pizzaController::readAllFromUser, Role.USER);
            get("/{id}", pizzaController::read, Role.USER);
            put("/{id}", pizzaController::update, Role.USER);
            delete("/{id}", pizzaController::delete, Role.USER);
        };
    }
}