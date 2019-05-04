package servlets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ua.edu.sumdu.nc.entities.bt.Entity;
import ua.edu.sumdu.nc.filter.Filter;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {
    private static Map<String, Filter<Entity>> searchFilters = new HashMap<>();

    static {
        // TODO static map filling
    }

    @PostMapping("/")
    public String handleRequest(@RequestBody String requestBody) {
        return null; // TODO
    }


}
