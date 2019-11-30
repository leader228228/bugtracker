package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.edu.sumdu.nc.validation.BTConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.converters.BTRequestConverterFactory;
import ua.edu.sumdu.nc.validation.issues.CreateIssueRequest;
import ua.edu.sumdu.nc.validation.projects.CreateProjectRequest;
import ua.edu.sumdu.nc.validation.replies.CreateReplyRequest;
import ua.edu.sumdu.nc.validation.users.CreateUserRequest;
import ua.edu.sumdu.nc.validation.issues.DeleteIssueRequest;
import ua.edu.sumdu.nc.validation.projects.DeleteProjectRequest;
import ua.edu.sumdu.nc.validation.replies.DeleteReplyRequest;
import ua.edu.sumdu.nc.validation.users.DeleteUserRequest;
import ua.edu.sumdu.nc.validation.issues.SearchIssuesRequest;
import ua.edu.sumdu.nc.validation.replies.SearchRepliesRequest;
import ua.edu.sumdu.nc.validation.users.SearchUsersRequest;
import ua.edu.sumdu.nc.validation.issues.UpdateIssueRequest;
import ua.edu.sumdu.nc.validation.replies.UpdateReplyRequest;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ua", "entities", "services"})
@PropertySource("classpath:/application.properties")
public class AppConfig
    extends AnnotationConfigWebApplicationContext implements WebApplicationInitializer, WebMvcConfigurer {

    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        context.scan("config");
        context.setBeanName("applicationContext");
        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcherServlet", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        Map<Class<? extends BTRequest>, Converter<String, BTRequest>> map = new HashMap<>();
        map.put(CreateIssueRequest.class,   (BTConverter) () -> CreateIssueRequest.class);
        map.put(CreateProjectRequest.class, (BTConverter) () -> CreateProjectRequest.class);
        map.put(CreateUserRequest.class,    (BTConverter) () -> CreateUserRequest.class);
        map.put(CreateReplyRequest.class,   (BTConverter) () -> CreateReplyRequest.class);
        map.put(DeleteIssueRequest.class,   (BTConverter) () -> DeleteIssueRequest.class);
        map.put(DeleteProjectRequest.class, (BTConverter) () -> DeleteProjectRequest.class);
        map.put(DeleteUserRequest.class,    (BTConverter) () -> DeleteUserRequest.class);
        map.put(DeleteReplyRequest.class,   (BTConverter) () -> DeleteReplyRequest.class);
        map.put(SearchIssuesRequest.class,  (BTConverter) () -> SearchIssuesRequest.class);
        map.put(SearchRepliesRequest.class, (BTConverter) () -> SearchRepliesRequest.class);
        map.put(SearchUsersRequest.class,   (BTConverter) () -> SearchUsersRequest.class);
        map.put(UpdateIssueRequest.class,   (BTConverter) () -> UpdateIssueRequest.class);
        map.put(UpdateReplyRequest.class,   (BTConverter) () -> UpdateReplyRequest.class);
        registry.addConverterFactory(new BTRequestConverterFactory(map));
    }
}
