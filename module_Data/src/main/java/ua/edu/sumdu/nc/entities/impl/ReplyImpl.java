package ua.edu.sumdu.nc.entities.impl;

import ua.edu.sumdu.nc.entities.bt.Reply;

public class ReplyImpl implements Reply {
    private long replyId;
    private String body;
    private long issueId;

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
    public void save() {
        // TODO
    }

    @Override
    public void delete() {
        // TODO
    }
}