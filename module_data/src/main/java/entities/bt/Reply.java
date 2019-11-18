package entities.bt;

import java.sql.Date;

public abstract class Reply implements Entity {
    protected long replyID;
    protected String body;
    protected long issueID;
    protected long authorID;
    protected Date created;

    public long getReplyID() {
        return replyID;
    }

    public void setReplyID(long replyID) {
        this.replyID = replyID;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getIssueID() {
        return issueID;
    }

    public void setIssueID(long issueID) {
        this.issueID = issueID;
    }

    public long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(long authorID) {
        this.authorID = authorID;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Reply{" +
            "replyID=" + replyID +
            ", body='" + body + '\'' +
            ", issueID=" + issueID +
            ", authorID=" + authorID +
            ", created=" + created +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reply reply = (Reply) o;

        if (replyID != reply.replyID) return false;
        if (issueID != reply.issueID) return false;
        if (authorID != reply.authorID) return false;
        if (!body.equals(reply.body)) return false;
        return created.equals(reply.created);
    }

    @Override
    public int hashCode() {
        int result = (int) (replyID ^ (replyID >>> 32));
        result = 31 * result + body.hashCode();
        result = 31 * result + (int) (issueID ^ (issueID >>> 32));
        result = 31 * result + (int) (authorID ^ (authorID >>> 32));
        result = 31 * result + created.hashCode();
        return result;
    }
}
