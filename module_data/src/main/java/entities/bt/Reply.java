package entities.bt;

public interface Reply extends Entity {
    long getReplyId();
    String getBody();
    void setBody(String body);
    long getIssueId();
    void setIssueId(long issueId);
    long getAuthorId();
    void setAuthorId(long authorId);
    void setReplyId(long replyId);
}
