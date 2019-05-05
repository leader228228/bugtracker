package ua.edu.sumdu.nc.main.config;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import ua.edu.sumdu.nc.controllers.SearchController;
import ua.edu.sumdu.nc.data.dao.DAO;
import ua.edu.sumdu.nc.data.dao.impl.DAOImpl;
import ua.edu.sumdu.nc.data.entities.bt.Issue;
import ua.edu.sumdu.nc.data.entities.bt.Project;
import ua.edu.sumdu.nc.data.filters.factory.FilterFactory;
import ua.edu.sumdu.nc.data.filters.factory.FilterFactoryImpl;
import ua.edu.sumdu.nc.data.filters.impl.issues.IssueByBodyFilter;
import ua.edu.sumdu.nc.data.filters.impl.issues.IssueByIdFilter;
import ua.edu.sumdu.nc.data.filters.impl.issues.IssueByReplyBodyFilter;
import ua.edu.sumdu.nc.data.filters.impl.issues.IssueByTitleFilter;
import ua.edu.sumdu.nc.data.filters.impl.projects.ProjectByIdFilter;
import ua.edu.sumdu.nc.data.filters.impl.projects.ProjectByNameFilter;
import ua.edu.sumdu.nc.data.parsers.Parser;
import ua.edu.sumdu.nc.data.parsers.impl.issues.AllIssuesParser;
import ua.edu.sumdu.nc.data.parsers.impl.projects.AllProjectsParser;

import java.net.URISyntaxException;

@Configuration
@ComponentScan(basePackages = "ua.edu.sumdu.nc")
public class AppConfig {
    private static ApplicationContext context = new AnnotationConfigApplicationContext("ua.edu.sumdu.nc");

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        AppConfig.context = context;
    }

    @Bean
    @Scope(scopeName = "singleton")
    public DAO DAO() {
        return new DAOImpl();
    }

    // Filters configurations
    @Bean(name = {"BTIssuesByBodySearchRequest", "IssueByBodyFilter"})
    @Scope(scopeName = "singleton")
    public IssueByBodyFilter issueByBodyFilter(@Qualifier(value = "allIssuesParser") Parser<Issue> parser, DAO dao) {
        return new IssueByBodyFilter(parser, dao);
    }

    @Bean(name = {"BTIssuesByIdSearchRequest", "IssueByIdFilter"})
    @Scope(scopeName = "singleton")
    public IssueByIdFilter issueByIdFilter(@Qualifier(value = "allIssuesParser") Parser<Issue> parser, DAO dao) {
        return new IssueByIdFilter(parser, dao);
    }

    @Bean(name = {"BTIssuesByReplySearchRequest", "IssueByReplyBodyFilter"})
    @Scope(scopeName = "singleton")
    public IssueByReplyBodyFilter issueByReplyBodyFilter(@Qualifier(value = "allIssuesParser") Parser<Issue> parser, DAO dao) {
        return new IssueByReplyBodyFilter(parser, dao);
    }

    @Bean(name = {"BTIssuesByNameSearchRequest", "IssueByTitleFilter"})
    @Scope(scopeName = "singleton")
    public IssueByTitleFilter issueByTitleFilter(@Qualifier(value = "allIssuesParser") Parser<Issue> parser, DAO dao) {
        return new IssueByTitleFilter(parser, dao);
    }

    @Bean(name = {"BTProjectsByIdSearchRequest", "ProjectByIdFilter"})
    @Scope(scopeName = "singleton")
    public ProjectByIdFilter projectByIdFilter(@Qualifier(value = "allProjectsParser") Parser<Project> parser, DAO dao) {
        return new ProjectByIdFilter(parser, dao);
    }

    @Bean(name = {"BTProjectsByNameSearchRequest", "ProjectByNameFilter"})
    @Scope(scopeName = "singleton")
    public ProjectByNameFilter projectByNameFilter(@Qualifier(value = "allProjectsParser") Parser<Project> parser, DAO dao) {
        return new ProjectByNameFilter(parser, dao);
    }

    // Parsers configuration
    @Bean(name = "AllIssuesParser")
    @Scope(scopeName = "singleton")
    public AllIssuesParser allIssuesParser() {
        return new AllIssuesParser();
    }

    @Bean(name = "AllProjectsParser")
    @Scope(scopeName = "singleton")
    public AllProjectsParser allProjectsParser() {
        return new AllProjectsParser();
    }

    @Bean(name = "FilterFactory")
    @Scope(scopeName = "singleton")
    public FilterFactory filterFactory() {
        return new FilterFactoryImpl();
    }

    @Bean(name = "BTRequestSchema")
    @Scope(scopeName = "singleton")
    public Schema schema() {
        try {
            return SchemaLoader
                    .builder()
                    .resolutionScope(SearchController.class.getResource("../../../../../json/schemas/").toURI())
                    .schemaClient(SchemaClient.classPathAwareClient())
                    .schemaJson(SearchController.class.getResource("../../../../../json/schemas/request/BTRequest.json").toURI())
                    .build()
                    .load()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
