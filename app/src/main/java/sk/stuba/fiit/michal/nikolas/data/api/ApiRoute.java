package sk.stuba.fiit.michal.nikolas.data.api;

/**
 * Created by Nikolas on 1.4.2016.
 */
public class ApiRoute {

    private String method;
    private String route;

    public ApiRoute(String method, String route) {
        this.method = method;
        this.route = route;
    }

    public String getMethod() {
        return method;
    }

    public String getRoute() {
        return route;
    }
}
