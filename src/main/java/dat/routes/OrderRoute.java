package dat.routes;

import dat.controllers.impl.OrderController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class OrderRoute {
    private final OrderController orderController = new OrderController();

    protected EndpointGroup getRoutes() {
        return () -> {
            post("/populate", orderController::populate, Role.ANYONE);
            post("/", orderController::create, Role.USER);
            get("/", orderController::readAll, Role.ANYONE);
            get("/user", orderController::readAllFromUser, Role.USER);
            get("/{id}", orderController::read, Role.USER);
            put("/{id}", orderController::update, Role.USER);
            delete("/{id}", orderController::delete, Role.USER);
        };
    }
}