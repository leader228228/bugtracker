package ua.edu.sumdu.nc.controllers.search;

import dao.DAO;
import org.everit.json.schema.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Controller;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SearchController extends Controller {

    public SearchController(@Autowired Schema schema, @Autowired dao.DAO DAO, @Qualifier("appConfig")ApplicationContext applicationContext) {
        super(schema, DAO, applicationContext);
    }

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Object handle(HttpServletRequest httpServletRequest) {
        // TODO
        return null;
    }
}
