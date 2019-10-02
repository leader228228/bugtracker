package config;

import dao.DAO;
import dao.impl.DAOImpl;
import entities.bt.*;
import entities.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.converters.RequestConverterFactory;
import ua.edu.sumdu.nc.converters.create.issues.CreateIssueRequestConverter;
import ua.edu.sumdu.nc.converters.create.projects.CreateProjectRequestConverter;
import ua.edu.sumdu.nc.converters.create.replies.CreateReplyRequestConverter;
import ua.edu.sumdu.nc.converters.create.users.CreateUserRequestConverter;
import ua.edu.sumdu.nc.converters.delete.issues.DeleteIssueRequestConverter;
import ua.edu.sumdu.nc.converters.delete.projects.DeleteProjectRequestConverter;
import ua.edu.sumdu.nc.converters.delete.replies.DeleteReplyRequestConverter;
import ua.edu.sumdu.nc.converters.delete.users.DeleteUserRequestConverter;
import ua.edu.sumdu.nc.converters.search.issues.IssueSearchRequestConverter;
import ua.edu.sumdu.nc.converters.search.replies.ReplySearchRequestConverter;
import ua.edu.sumdu.nc.converters.search.users.UserSearchRequestConverter;
import ua.edu.sumdu.nc.converters.update.issues.IssueUpdateRequestConverter;
import ua.edu.sumdu.nc.converters.update.replies.ReplyUpdateRequestConverter;
import ua.edu.sumdu.nc.validation.BTRequest;
import ua.edu.sumdu.nc.validation.create.issues.CreateIssueRequest;
import ua.edu.sumdu.nc.validation.create.projects.CreateProjectRequest;
import ua.edu.sumdu.nc.validation.create.replies.CreateReplyRequest;
import ua.edu.sumdu.nc.validation.create.users.CreateUserRequest;
import ua.edu.sumdu.nc.validation.delete.issues.DeleteIssueRequest;
import ua.edu.sumdu.nc.validation.delete.projects.DeleteProjectRequest;
import ua.edu.sumdu.nc.validation.delete.replies.DeleteReplyRequest;
import ua.edu.sumdu.nc.validation.delete.users.DeleteUserRequest;
import ua.edu.sumdu.nc.validation.search.issues.SearchIssuesRequest;
import ua.edu.sumdu.nc.validation.search.replies.SearchRepliesRequest;
import ua.edu.sumdu.nc.validation.search.users.SearchUsersRequest;
import ua.edu.sumdu.nc.validation.update.issues.UpdateIssueRequest;
import ua.edu.sumdu.nc.validation.update.replies.UpdateReplyRequest;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ua")
@PropertySource("classpath:/application.properties")
public class AppConfig
    extends AnnotationConfigWebApplicationContext
    implements WebApplicationInitializer, WebMvcConfigurer {

    private Environment env;

    public AppConfig(Environment env) {
        this.env = env;
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    @Bean(name = "DAO")
    @Scope(scopeName = "singleton")
    public DAO DAO() throws ClassNotFoundException {
        Class.forName(env.getProperty("db.connection.driver"));
        return new DAOImpl(
                env.getProperty("db.connection.url"),
                env.getProperty("db.connection.user"),
                env.getProperty("db.connection.password")
        );
    }

    @Bean(name = "Issue")
    @Scope(scopeName = "prototype")
    public Issue issue(@Autowired DAO DAO) {
        return new IssueImpl(DAO);
    }

    @Bean(name = "Reply")
    @Scope(scopeName = "prototype")
    public Reply reply(@Autowired DAO DAO) {
        return new ReplyImpl(DAO);
    }

    @Bean(name = "Project")
    @Scope(scopeName = "prototype")
    public Project project(@Autowired DAO DAO) {
        return new ProjectImpl(DAO);
    }

    @Bean(name = "User")
    @Scope(scopeName = "prototype")
    public User user(DAO DAO) {
        return new UserImpl(DAO);
    }

    @Bean(name = "IssueStatus")
    @Scope(scopeName = "prototype")
    public IssueStatus issueStatus(@Autowired DAO DAO) {
        return new IssueStatusImpl(DAO);
    }

    @Bean(name = "Utils")
    @Scope(scopeName = "singleton")
    public Utils utils(@Qualifier("appConfig") ApplicationContext applicationContext) {
        return new Utils(applicationContext);
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

    @Override
    public void addFormatters(FormatterRegistry registry) {
        Map<Class<? extends BTRequest>, Converter<String, BTRequest>> map = new HashMap<>();
        map.put(CreateIssueRequest.class, new CreateIssueRequestConverter());
        map.put(CreateProjectRequest.class, new CreateProjectRequestConverter());
        map.put(CreateUserRequest.class, new CreateUserRequestConverter());
        map.put(CreateReplyRequest.class, new CreateReplyRequestConverter());

        map.put(DeleteIssueRequest.class, new DeleteIssueRequestConverter());
        map.put(DeleteProjectRequest.class, new DeleteProjectRequestConverter());
        map.put(DeleteUserRequest.class, new DeleteUserRequestConverter());
        map.put(DeleteReplyRequest.class, new DeleteReplyRequestConverter());

        map.put(SearchIssuesRequest.class, new IssueSearchRequestConverter());
        map.put(SearchRepliesRequest.class, new ReplySearchRequestConverter());
        map.put(SearchUsersRequest.class, new UserSearchRequestConverter());

        map.put(UpdateIssueRequest.class, new IssueUpdateRequestConverter());
        map.put(UpdateReplyRequest.class, new ReplyUpdateRequestConverter());

        registry.addConverterFactory(new RequestConverterFactory(map));
    }
}
