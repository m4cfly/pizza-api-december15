package dat.routes;

import dat.controllers.impl.PizzaOrderController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;


public class PizzaOrderRoute {
    private final PizzaOrderController pizzaOrderController = new PizzaOrderController();

    public EndpointGroup getRoutes() {
        return () -> {
            post("/populate", pizzaOrderController::populate, Role.ANYONE);
            post("/", pizzaOrderController::create, Role.ANYONE);
            get("/", pizzaOrderController::readAll, Role.ANYONE);
            get("/mine", pizzaOrderController::readAllFromUser,Role.ANYONE);
            get("/{id}", pizzaOrderController::read, Role.ANYONE);
            put("/{id}", pizzaOrderController::update, Role.ANYONE);
            delete("/{id}", pizzaOrderController::delete, Role.ANYONE);
        };
    }
}