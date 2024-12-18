package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final PizzaOrderRoute pizzaOrderRoute = new PizzaOrderRoute();



    public EndpointGroup getRoutes() {
        return () -> {
                path("/pizza-orders", pizzaOrderRoute.getRoutes());
        };
    }
}
