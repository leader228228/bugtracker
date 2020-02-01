package ua.edu.sumdu.nc.services;

import ua.edu.sumdu.nc.entities.Reply;

import java.sql.SQLException;
import java.util.Collection;

public interface ReplyService {

    Reply createReply(long authorID, String body, long issueID) throws SQLException;

    boolean deleteReplies(Collection<Long> repliesIDs) throws SQLException;

    Collection<Reply> getAll() throws SQLException;

    Collection<Reply> searchRepliesByAuthorsIDs(long[] authorsIDs) throws SQLException;

    Collection<Reply> searchRepliesByText(String text) throws SQLException;

    Collection<Reply> searchRepliesByIssuesIDs(long[] issuesIDs) throws SQLException;
}
