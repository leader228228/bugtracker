package config;

import dao.DAO;
import dao.impl.DAOImpl;
import entities.bt.Issue;
import entities.bt.Project;
import entities.impl.IssueImpl;
import org.json.JSONObject;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.controllers.search.SearchController;
import ua.edu.sumdu.nc.db.dbparsers.issues.AllIssuesDBParser;
import ua.edu.sumdu.nc.db.dbparsers.projects.AllProjectsDBParser;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ua.edu.sumdu.nc.db.creators.CreatorSelector;
import ua.edu.sumdu.nc.db.dbparsers.DBParser;
import ua.edu.sumdu.nc.db.filters.FilterSelector;
import ua.edu.sumdu.nc.db.filters.issues.IssueByBodyFilter;
import ua.edu.sumdu.nc.db.filters.issues.IssueByIdFilter;
import ua.edu.sumdu.nc.db.filters.issues.IssueByReplyBodyFilter;
import ua.edu.sumdu.nc.db.filters.issues.IssueByTitleFilter;
import ua.edu.sumdu.nc.db.filters.projects.ProjectByIdFilter;
import ua.edu.sumdu.nc.db.filters.projects.ProjectByNameFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "ua")
public class AppConfig extends AnnotationConfigWebApplicationContext implements WebApplicationInitializer {

    @Bean(name = "DAO")
    @Scope(scopeName = "singleton")
    public DAO DAO() {
        return new DAOImpl();
    }

    // Filters configurations
    @Bean(name = {"BTIssuesByBodySearchRequest", "IssueByBodyFilter"})
    @Scope(scopeName = "singleton")
    public IssueByBodyFilter issueByBodyFilter(@Qualifier(value = "AllIssuesParser") AllIssuesDBParser parser, DAO dao) {
        return new IssueByBodyFilter(parser, dao);
    }

    @Bean(name = {"BTIssuesByIdSearchRequest", "IssueByIdFilter"})
    @Scope(scopeName = "singleton")
    public IssueByIdFilter issueByIdFilter(@Qualifier(value = "AllIssuesParser") DBParser<Issue> parser, DAO dao) {
        return new IssueByIdFilter(parser, dao);
    }

    @Bean(name = {"BTIssuesByReplySearchRequest", "IssueByReplyBodyFilter"})
    @Scope(scopeName = "singleton")
    public IssueByReplyBodyFilter issueByReplyBodyFilter(
            @Qualifier(value = "AllIssuesParser") DBParser<Issue> parser, DAO dao) {
        return new IssueByReplyBodyFilter(parser, dao);
    }

    @Bean(name = {"BTIssuesByNameSearchRequest", "IssueByTitleFilter"})
    @Scope(scopeName = "singleton")
    public IssueByTitleFilter issueByTitleFilter(@Qualifier(value = "AllIssuesParser") DBParser<Issue> parser, DAO dao) {
        return new IssueByTitleFilter(parser, dao);
    }

    @Bean(name = {"BTProjectsByIdSearchRequest", "ProjectByIdFilter"})
    @Scope(scopeName = "singleton")
    public ProjectByIdFilter projectByIdFilter
            (@Qualifier(value = "AllProjectsParser") DBParser<Project> parser, DAO dao) {
        return new ProjectByIdFilter(parser, dao);
    }

    @Bean(name = {"BTProjectsByNameSearchRequest", "ProjectByNameFilter"})
    @Scope(scopeName = "singleton")
    public ProjectByNameFilter projectByNameFilter
            (@Qualifier(value = "AllProjectsParser") DBParser<Project> parser, DAO dao) {
        return new ProjectByNameFilter(parser, dao);
    }

    // Parsers configuration
    @Bean(name = "AllIssuesParser")
    @Scope(scopeName = "singleton")
    public AllIssuesDBParser allIssuesParser(@Autowired DAO dao, @Autowired Schema schema) {
        return new AllIssuesDBParser(dao, schema);
    }

    @Bean(name = "AllProjectsParser")
    @Scope(scopeName = "singleton")
    public AllProjectsDBParser allProjectsParser() {
        return new AllProjectsDBParser();
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
            return SchemaLoader
                    .builder()
                    .schemaClient(SchemaClient.classPathAwareClient())
                    /*.resolutionScope("classpath://json/schemas/")*/
                    .resolutionScope(AppConfig.class.getResource(
                            "../json/schemas/").toURI())
                    .schemaJson(schemaJson)
                    .build()
                    .load()
                    .build();
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

    @Bean(name = "FilterSelector")
    @Scope(scopeName = "singleton")
    public FilterSelector filterSelector(@Qualifier("appConfig") @Autowired ApplicationContext applicationContext) {
        return new FilterSelector(applicationContext);
    }

    @Bean(name = {"CreatorSelector"})
    @Scope(scopeName = "singleton")
    public CreatorSelector creatorSelector(@Qualifier("appConfig") @Autowired ApplicationContext applicationContext) {
        return new CreatorSelector(applicationContext);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("/WEB-INF/dispatcherServlet-servlet.xml");

        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcherServlet", new DispatcherServlet(appContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");
    }
}
