--Users
INSERT INTO BT_USERS(USER_ID, LOGIN, PASSWORD, FIRST_NAME, LAST_NAME)
VALUES(0, 'unassigned', 'unassigned', 'unassigned', 'unassigned');

INSERT INTO BT_USERS(USER_ID, LOGIN, PASSWORD, FIRST_NAME, LAST_NAME)
VALUES(1, 'mybi', '12345', 'Mykhailo', 'Birintsev');

INSERT INTO BT_USERS(USER_ID, LOGIN, PASSWORD, FIRST_NAME, LAST_NAME)
VALUES(2, 'iviv', '67891', 'Ivan', 'Ivanov');

INSERT INTO BT_USERS(USER_ID, LOGIN, PASSWORD, FIRST_NAME, LAST_NAME)
VALUES(3, 'pepe', '01112', 'Petr', 'Petrov');

INSERT INTO BT_USERS(USER_ID, LOGIN, PASSWORD, FIRST_NAME, LAST_NAME)
VALUES(4, 'olol', '13141', 'Oleksandr', 'Oleksandrov');

INSERT INTO BT_USERS(USER_ID, LOGIN, PASSWORD, FIRST_NAME, LAST_NAME)
VALUES(5, 'bobo', '51617', 'Bohdan', 'Bohdanov');

--Issue statuses
INSERT INTO BT_ISSUE_STATUSES (STATUS_ID, "value")
VALUES (0, 'New');

INSERT INTO BT_ISSUE_STATUSES (STATUS_ID, "value")
VALUES (1, 'Open');

INSERT INTO BT_ISSUE_STATUSES (STATUS_ID, "value")
VALUES (2, 'In progress');

INSERT INTO BT_ISSUE_STATUSES (STATUS_ID, "value")
VALUES (3, 'Additional info required');

INSERT INTO BT_ISSUE_STATUSES (STATUS_ID, "value")
VALUES (4, 'Info recieved');

INSERT INTO BT_ISSUE_STATUSES (STATUS_ID, "value")
VALUES (5, 'Resolved');

INSERT INTO BT_ISSUE_STATUSES (STATUS_ID, "value")
VALUES (6, 'Closed');

INSERT INTO BT_ISSUE_STATUSES (STATUS_ID, "value")
VALUES (7, 'Not a bug');

INSERT INTO BT_ISSUE_STATUSES (STATUS_ID, "value")
VALUES (8, 'Duplicate');

INSERT INTO BT_ISSUE_STATUSES (STATUS_ID, "value")
VALUES (9, 'In progress');

--Projects
INSERT INTO BT_PROJECTS(PROJECT_ID, NAME)
VALUES(1, 'Project1');

INSERT INTO BT_PROJECTS(PROJECT_ID, NAME)
VALUES(2, 'Project2');

--Issues
INSERT INTO BT_ISSUES(ISSUE_ID, REPORTER_ID, ASSIGNEE_ID, CREATED, STATUS_ID, PROJECT_ID, "body", TITLE)
VALUES(1, 1, 1, SYSDATE, 1, 1, 'Dear support team, I am unable to login into the system', 'Logging troubles');

INSERT INTO BT_ISSUES(ISSUE_ID, REPORTER_ID, ASSIGNEE_ID, CREATED, STATUS_ID, PROJECT_ID, "body", TITLE)
VALUES(2, 2, 2, SYSDATE + 100, 2, 2, 'Could you give me a piece of information about...', 'I need some info');

INSERT INTO BT_ISSUES(ISSUE_ID, REPORTER_ID, ASSIGNEE_ID, CREATED, STATUS_ID, PROJECT_ID, "body", TITLE)
VALUES(3, 3, 1, SYSDATE + 200, 5, 2, 'Could you send me a report about the affected users', 'Report request');

INSERT INTO BT_ISSUES(ISSUE_ID, REPORTER_ID, ASSIGNEE_ID, CREATED, STATUS_ID, PROJECT_ID, "body", TITLE)
VALUES(4, 4, 5, SYSDATE + 300, 6, 1, 'Do we can to create a new feature? It would be ,much comfortabel to...', 'New feature suggestion');

INSERT INTO BT_ISSUES(ISSUE_ID, REPORTER_ID, ASSIGNEE_ID, CREATED, STATUS_ID, PROJECT_ID, "body", TITLE)
VALUES(5, 1, 1, SYSDATE + 400, 1, 2, 'Hello, suuport team, the system fails to send a message with attachments', 'Attachments fail issue creating');

--Replies
INSERT INTO BT_REPLIES(REPLY_ID, "body", ISSUE_ID, AUTHOR_ID, CREATED)
VALUES(1, 'Dear colleague, please, send a screenshoot with the exception the system shows', 1, 1, SYSDATE + 500);

INSERT INTO BT_REPLIES(REPLY_ID, "body", ISSUE_ID, AUTHOR_ID, CREATED)
VALUES(2, 'There we some troubles from our side. Please, take our apologies', 2, 2, SYSDATE + 600);

INSERT INTO BT_REPLIES(REPLY_ID, "body", ISSUE_ID, AUTHOR_ID, CREATED)
VALUES(3, 'What the users do you want to recieve the information about? Do you mean the users wew affected by the recent data leak?', 3, 3, SYSDATE + 700);

INSERT INTO BT_REPLIES(REPLY_ID, "body", ISSUE_ID, AUTHOR_ID, CREATED)
VALUES(4, 'Hello, it would be good to have such functional in the system, but first I must to discuss the proposition with the management', 4, 4, SYSDATE + 800);

INSERT INTO BT_REPLIES(REPLY_ID, "body", ISSUE_ID, AUTHOR_ID, CREATED)
VALUES(5, 'Please, send me a screenshot describing the problem', 5, 5, SYSDATE + 800);

commit;