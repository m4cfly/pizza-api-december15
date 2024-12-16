package dat.routes;

import dat.controllers.impl.OrderLineController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class OrderLineRoute {
    private final OrderLineController orderLineController = new OrderLineController();

    protected EndpointGroup getRoutes() {
        return () -> {
            post("/populate", orderLineController::populate, Role.ANYONE);
            post("/", orderLineController::create, Role.USER);
            get("/", orderLineController::readAll, Role.ANYONE);
            get("/user", orderLineController::readAllFromUser, Role.USER);
            get("/{id}", orderLineController::read, Role.USER);
            put("/{id}", orderLineController::update, Role.USER);
            delete("/{id}", orderLineController::delete, Role.USER);
        };
    }
}