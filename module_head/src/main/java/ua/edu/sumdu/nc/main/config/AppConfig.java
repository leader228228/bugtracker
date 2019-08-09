package ua.edu.sumdu.nc.main.config;

import dao.DAO;
import dao.impl.DAOImpl;
import entities.bt.Issue;
import entities.bt.Project;
import entities.impl.IssueImpl;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.controllers.search.SearchController;
import ua.edu.sumdu.nc.db.creators.CreatorSelector;
import ua.edu.sumdu.nc.db.dbparsers.DBParser;
import ua.edu.sumdu.nc.db.dbparsers.issues.AllIssuesDBParser;
import ua.edu.sumdu.nc.db.dbparsers.projects.AllProjectsDBParser;
import ua.edu.sumdu.nc.db.filters.FilterSelector;
import ua.edu.sumdu.nc.db.filters.issues.IssueByBodyFilter;
import ua.edu.sumdu.nc.db.filters.issues.IssueByIdFilter;
import ua.edu.sumdu.nc.db.filters.issues.IssueByReplyBodyFilter;
import ua.edu.sumdu.nc.db.filters.issues.IssueByTitleFilter;
import ua.edu.sumdu.nc.db.filters.projects.ProjectByIdFilter;
import ua.edu.sumdu.nc.db.filters.projects.ProjectByNameFilter;

import java.net.URISyntaxException;

@Configuration
@ComponentScan(basePackages = "ua.edu.sumdu.nc")
public class AppConfig {
    private static ApplicationContext context = new AnnotationConfigApplicationContext("ua.edu.sumdu.nc");

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

    @Bean(name = "Utils")
    @Scope(scopeName = "singleton")
    public Utils utils(@Autowired ApplicationContext applicationContext, DAO dao) {
        return new Utils(applicationContext, dao);
    }

    @Bean(name = "FilterSelector")
    @Scope(scopeName = "singleton")
    public FilterSelector filterSelector(@Autowired ApplicationContext applicationContext) {
        return new FilterSelector(applicationContext);
    }

    @Bean(name = {"CreatorSelector"})
    @Scope(scopeName = "singleton")
    public CreatorSelector creatorSelector(@Autowired ApplicationContext applicationContext) {
        return new CreatorSelector(applicationContext);
    }
}
