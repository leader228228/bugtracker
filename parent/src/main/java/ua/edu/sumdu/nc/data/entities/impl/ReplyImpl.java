package ua.edu.sumdu.nc.data.entities.impl;

import ua.edu.sumdu.nc.data.entities.bt.Reply;

public class ReplyImpl implements Reply {
    private long replyId;
    private String body;
    private long issueId;
    private long authorId;

    public ReplyImpl() {
    }

    public ReplyImpl(long replyId) {
        this.replyId = replyId;
    }

    public ReplyImpl(long replyId, String body) {
        this.replyId = replyId;
        this.body = body;
    }

    public ReplyImpl(long replyId, String body, long issueId) {
        this.replyId = replyId;
        this.body = body;
        this.issueId = issueId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public long getReplyId() {
        return replyId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getIssueId() {
        return issueId;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    @Override
    public long getAuthorId() {
        return authorId;
    }

    @Override
    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }
}
