SET 'auto.offset.reset' = 'earliest';

PRINT 'member_signups' FROM BEGINNING;

CREATE STREAM member_signups (
    lastname VARCHAR,
    firstname VARCHAR,
    email_notification BOOLEAN
) WITH (
    KAFKA_TOPIC = 'member_signups',
    VALUE_FORMAT = 'DELIMITED'
);

CREATE STREAM member_signups_email AS
       SELECT lastname, firstname
       FROM member_signups
       WHERE email_notification = true;

PRINT 'MEMBER_SIGNUPS_EMAIL' FROM BEGINNING;

-------------------------------------------

PRINT 'member_signups' from beginning;
PRINT 'member_contact' from beginning;

CREATE STREAM member_signups (
    lastname VARCHAR,
    firstname VARCHAR
) WITH (
    KAFKA_TOPIC = 'member_signups',
    VALUE_FORMAT = 'DELIMITED'
);

CREATE STREAM member_contact (
    email VARCHAR
) WITH (
    KAFKA_TOPIC = 'member_contact',
    VALUE_FORMAT = 'DELIMITED'
);

CREATE STREAM member_email_lists AS
    SELECT member_signups.firstname, member_signups.lastname, member_contact.email
    FROM member_signups
    INNER JOIN member_contact
    WITHIN 365 DAYS
    ON member_signups.rowkey = member_contact.rowkey;

print 'MEMBER_EMAIL_LIST' from beginning;
