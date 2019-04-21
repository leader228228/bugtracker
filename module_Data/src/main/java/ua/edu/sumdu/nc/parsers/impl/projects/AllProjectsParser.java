package ua.edu.sumdu.nc.parsers.impl.projects;

import ua.edu.sumdu.nc.entities.bt.Issue;
import ua.edu.sumdu.nc.entities.bt.Project;
import ua.edu.sumdu.nc.entities.impl.ProjectImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class AllProjectsParser implements ProjectParser {
    /**
     *  Parses the {@code ResultSet} extracting {@code Project}s from it
     *
     * @param           resultSet the result of executing the query {@code SELECT * FROM BT_PROJECTS}
     *
     * */
    @Override
    public Collection<Project> parse(ResultSet resultSet) throws SQLException {
        Collection<Project> result = new LinkedList<>();
        while (resultSet.next()) {
            ProjectImpl project = new ProjectImpl(resultSet.getLong("PROJECT_ID")
                    , resultSet.getString("NAME")
                    , resultSet.getLong("ADMIN_ID"));
        }
        return result;
    }
}