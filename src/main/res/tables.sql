drop table bt_users cascade constraints;
drop table bt_projects cascade constraints;
drop table bt_issues cascade constraints;
drop table bt_replies cascade constraints;
drop table bt_issue_statuses cascade constraints;
drop sequence bt_issues_ids_seq;
drop sequence bt_issue_statuses_ids_seq;
drop sequence bt_projects_ids_seq;
drop sequence bt_users_ids_seq;
drop sequence bt_replies_ids_seq;

create table bt_users(
	user_id number primary key,
	login varchar2 (100 char) not null,
	password varchar2 (100 char) not null,
	first_name varchar2 (100 char) not null,
	last_name varchar2 (100 char) not null,
	constraint uniq_login unique(login)
);

create table bt_projects(
	project_id number primary key,
	name varchar2 (100 char) not null
);

create table bt_issue_statuses(
	status_id number primary key,
	"value" varchar2 (100 char) not null
);

create table bt_issues(
	issue_id number primary key,
	reporter_id number not null,
	assignee_id number default 0,
	created date default sysdate not null,
	status_id number default 0 not null,
	project_id number not null,
	"body" varchar2(4000 char) not null,
	title varchar2(200 char) not null,
	constraint fk_project_id foreign key (project_id) references bt_projects(project_id) on delete cascade,
	constraint fk_reporter_id foreign key (reporter_id) references bt_users(user_id) on delete cascade,
	constraint fk_status_id foreign key (status_id) references bt_issue_statuses(status_id) on delete cascade,
	constraint fk_assignee_id foreign key(assignee_id) references bt_users(user_id) on delete cascade
);

create table bt_replies(
	reply_id number primary key,
	"body" varchar2(4000 char) not null,
	issue_id number not null,
	author_id number not null,
	created date default sysdate not null,
	constraint fk_issue_id foreign key(issue_id) references bt_issues(issue_id) on delete cascade,
	constraint fk_author_id foreign key(author_id) references bt_users(user_id) on delete cascade
);

create sequence bt_issues_ids_seq
  start with 1
  increment by 1;

CREATE TRIGGER bt_issues_ids_trig
  BEFORE INSERT ON bt_issues
  FOR EACH ROW
BEGIN
  IF :new.issue_id IS NULL THEN
    :new.issue_id := bt_issues_ids_seq.nextval;
  END IF;
END;

create sequence bt_replies_ids_seq
  start with 1
  increment by 1;

CREATE TRIGGER bt_replies_ids_trig
  BEFORE INSERT ON bt_replies
  FOR EACH ROW
BEGIN
  IF :new.reply_id IS NULL THEN
    :new.reply_id := bt_replies_ids_seq.nextval;
  END IF;
END;

create sequence bt_projects_ids_seq
  start with 1
  increment by 1;

CREATE TRIGGER bt_projects_ids_trig
  BEFORE INSERT ON bt_projects
  FOR EACH ROW
BEGIN
  IF :new.project_id IS NULL THEN
    :new.project_id := bt_projects_ids_seq.nextval;
  END IF;
END;

create sequence bt_users_ids_seq
  start with 1
  increment by 1;

CREATE TRIGGER bt_users_ids_trig
  BEFORE INSERT ON bt_users
  FOR EACH ROW
BEGIN
  IF :new.user_id IS NULL THEN
    :new.user_id := bt_users_ids_seq.nextval;
  END IF;
END;

create sequence bt_issue_statuses_ids_seq
  start with 1
  increment by 1;

CREATE TRIGGER bt_issue_statuses_ids_trig
  BEFORE INSERT ON bt_issue_statuses
  FOR EACH ROW
BEGIN
  IF :new.status_id IS NULL THEN
    :new.status_id := bt_issue_statuses_ids_seq.nextval;
  END IF;
END;