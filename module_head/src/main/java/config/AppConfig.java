package config;

import dao.DAO;
import dao.impl.DAOImpl;
import entities.bt.Issue;
import entities.impl.IssueImpl;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ua.edu.sumdu.nc.Utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ua")
public class AppConfig extends AnnotationConfigWebApplicationContext implements WebApplicationInitializer {

    @Bean(name = "DAO")
    @Scope(scopeName = "singleton")
    public DAO DAO() {
        return new DAOImpl();
    }

    @Bean(name = "BTRequestSchema")
    @Scope(scopeName = "singleton")
    public Schema schema() {
        try {
            JSONObject schemaJson;
            try (BufferedInputStream stream = new BufferedInputStream(AppConfig.class.getResourceAsStream(
                    "../json/schemas/BTRequest.json"))) {
                StringBuilder stringBuilder = new StringBuilder();
                int readByte;
                List<Character> buffer = new LinkedList<>();
                while ((readByte = stream.read()) != -1) {
                    buffer.add((char) readByte);
                }
                buffer.forEach(stringBuilder::append);
                schemaJson = new JSONObject(stringBuilder.toString());
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("Can not find root schema. Expected location:" + AppConfig.class.getResource(
                        "../json/schemas/BTRequest.json").toURI());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            /*Schema schema =*/return SchemaLoader
                    .builder()

                    /*.resolutionScope("classpath://json/schemas/")*/
                    .resolutionScope(AppConfig.class.getResource(
                            "../json/schemas/").toURI())
                    .schemaJson(schemaJson)

                    .schemaClient(url -> {
                        InputStream is = AppConfig.class.getResourceAsStream("../json/schemas/" + url);
                        if (is == null) {
                            throw new RuntimeException("url="+url);
                        } else {
                            return is;
                        }
                    })
                    .draftV7Support()
                    /*.schemaClient(SchemaClient.classPathAwareClient())*/
                    .build()
                    .load()
                    .build();
                    /*if (true) { // debug
                        throw new RuntimeException(schema.toString());
                    } else {
                        return schema;
                    }*/
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean(name = "Issue")
    @Scope(scopeName = "prototype")
    public Issue issue() {
        return new IssueImpl();
    }

    @Bean(name = "Utils")
    @Scope(scopeName = "singleton")
    public Utils utils(@Qualifier("appConfig") @Autowired ApplicationContext applicationContext, DAO dao) {
        return new Utils(applicationContext, dao);
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        context.scan("config");
        context.setBeanName("applicationContext");
        //XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        //appContext.setConfigLocation("/WEB-INF/dispatcherServlet-servlet.xml");
        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcherServlet", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");
    }
}
