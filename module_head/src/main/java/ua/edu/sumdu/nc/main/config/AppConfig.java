package ua.edu.sumdu.nc.main.config;

import dao.DAO;
import dao.impl.DAOImpl;
import dbparsers.DBParser;
import dbparsers.impl.issues.AllIssuesDBParser;
import dbparsers.impl.projects.AllProjectsDBParser;
import entities.bt.Issue;
import entities.bt.Project;
import entities.impl.IssueImpl;
import filters.factory.FilterFactory;
import filters.factory.FilterFactoryImpl;
import filters.impl.issues.IssueByBodyFilter;
import filters.impl.issues.IssueByIdFilter;
import filters.impl.issues.IssueByReplyBodyFilter;
import filters.impl.issues.IssueByTitleFilter;
import filters.impl.projects.ProjectByIdFilter;
import filters.impl.projects.ProjectByNameFilter;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.controllers.create.parsers.InputParser;
import ua.edu.sumdu.nc.controllers.create.parsers.IssueInputParser;
import ua.edu.sumdu.nc.controllers.search.SearchController;

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

    @Bean(name = "DAO")
    @Scope(scopeName = "singleton")
    public DAO DAO() {
        return new DAOImpl();
    }

    // Filters configurations
    @Bean(name = {"BTIssuesByBodySearchRequest", "IssueByBodyFilter"})
    @Scope(scopeName = "singleton")
    public IssueByBodyFilter issueByBodyFilter(@Qualifier(value = "AllIssuesParser") DBParser<Issue> parser, DAO dao) {
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
    public AllIssuesDBParser allIssuesParser() {
        return new AllIssuesDBParser();
    }

    @Bean(name = "AllProjectsParser")
    @Scope(scopeName = "singleton")
    public AllProjectsDBParser allProjectsParser() {
        return new AllProjectsDBParser();
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
                    .resolutionScope(SearchController.class.getResource(
                            "../../../../../json/schemas/").toURI())
                    .schemaClient(SchemaClient.classPathAwareClient())
                    .schemaJson(SearchController.class.getResource(
                            "../../../../../json/schemas/request/BTRequest.json").toURI())
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

    @Bean(name = "IssueParser")
    @Scope(scopeName = "singleton")
    public InputParser<JSONObject, Issue> issueParser(Utils utils, ApplicationContext applicationContext) {
        return new IssueInputParser(utils, applicationContext);
    }

    @Bean(name = "Utils")
    @Scope(scopeName = "singleton")
    public Utils utils(@Autowired ApplicationContext applicationContext) {
        return new Utils(applicationContext);
    }

}
