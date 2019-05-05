package ua.edu.sumdu.nc.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import ua.edu.sumdu.nc.data.dao.DAO;
import ua.edu.sumdu.nc.data.dao.impl.DAOImpl;
import ua.edu.sumdu.nc.data.entities.bt.Issue;
import ua.edu.sumdu.nc.data.entities.bt.Project;
import ua.edu.sumdu.nc.data.filters.impl.issues.IssueByBodyFilter;
import ua.edu.sumdu.nc.data.filters.impl.issues.IssueByIdFilter;
import ua.edu.sumdu.nc.data.filters.impl.issues.IssueByReplyBodyFilter;
import ua.edu.sumdu.nc.data.filters.impl.issues.IssueByTitleFilter;
import ua.edu.sumdu.nc.data.filters.impl.projects.ProjectByIdFilter;
import ua.edu.sumdu.nc.data.filters.impl.projects.ProjectByNameFilter;
import ua.edu.sumdu.nc.data.parsers.Parser;
import ua.edu.sumdu.nc.data.parsers.impl.issues.AllIssuesParser;
import ua.edu.sumdu.nc.data.parsers.impl.projects.AllProjectsParser;

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
    @Bean
    @Scope(scopeName = "singleton")
    public IssueByBodyFilter issueByBodyFilter(Parser<Issue> parser, DAO dao) {
        return new IssueByBodyFilter(parser, dao);
    }

    @Bean
    @Scope(scopeName = "singleton")
    public IssueByIdFilter issueByIdFilter(Parser<Issue> parser, DAO dao) {
        return new IssueByIdFilter(parser, dao);
    }

    @Bean
    @Scope(scopeName = "singleton")
    public IssueByReplyBodyFilter issueByReplyBodyFilter(Parser<Issue> parser, DAO dao) {
        return new IssueByReplyBodyFilter(parser, dao);
    }

    @Bean
    @Scope(scopeName = "singleton")
    public IssueByTitleFilter issueByTitleFilter(Parser<Issue> parser, DAO dao) {
        return new IssueByTitleFilter(parser, dao);
    }

    @Bean
    @Scope(scopeName = "singleton")
    public ProjectByIdFilter projectByIdFilter(Parser<Project> parser, DAO dao) {
        return new ProjectByIdFilter(parser, dao);
    }

    @Bean
    @Scope(scopeName = "singleton")
    public ProjectByNameFilter projectByNameFilter(Parser<Project> parser, DAO dao) {
        return new ProjectByNameFilter(parser, dao);
    }
    // Parsers configuration

    @Bean
    @Scope(scopeName = "singleton")
    public AllIssuesParser allIssuesParser() {
        return new AllIssuesParser();
    }

    @Bean
    @Scope(scopeName = "singleton")
    public AllProjectsParser allProjectsParser() {
        return new AllProjectsParser();
    }
}
