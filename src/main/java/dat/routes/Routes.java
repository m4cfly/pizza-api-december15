package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final TodoRoute todoRoute = new TodoRoute();
    private final PizzaRoute pizzaRoute = new PizzaRoute();
    private final OrderRoute orderRoute = new OrderRoute();
    private final OrderLineRoute orderLineRoute = new OrderLineRoute();

    public EndpointGroup getRoutes() {
        return () -> {
                path("/todos", todoRoute.getRoutes());
                path("/pizzas", pizzaRoute.getRoutes());
                path("/orders", orderRoute.getRoutes());
                path("/orderlines", orderLineRoute.getRoutes());
        };
    }
}
