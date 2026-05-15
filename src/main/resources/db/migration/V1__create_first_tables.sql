CREATE TABLE planning.clients
(
    id      UUID NOT NULL,
    name    VARCHAR(255),
    email   VARCHAR(255),
    phone   VARCHAR(255),
    user_fk UUID NOT NULL,
    CONSTRAINT pk_clients PRIMARY KEY (id)
);

CREATE TABLE planning.kanban_columns
(
    id         UUID NOT NULL,
    name       VARCHAR(255),
    position   INTEGER,
    project_fk UUID,
    version    BIGINT,
    CONSTRAINT pk_kanban_columns PRIMARY KEY (id)
);

CREATE TABLE planning.kanban_tasks
(
    id          UUID NOT NULL,
    title       VARCHAR(255),
    description TEXT,
    position    INTEGER,
    column_fk   UUID,
    version    BIGINT,
    CONSTRAINT pk_kanban_tasks PRIMARY KEY (id)
);

CREATE TABLE planning.projects
(
    id                UUID NOT NULL,
    title             VARCHAR(255),
    description       VARCHAR(255),
    platform          VARCHAR(255),
    status            VARCHAR(255),
    type              VARCHAR(255),
    minimum_value     DECIMAL,
    maximum_value     DECIMAL,
    closed_value      DECIMAL,
    delivery_forecast date,
    delivery_date     date,
    annotation        TEXT,
    is_personal_project BOOLEAN,
    user_fk           UUID NOT NULL,
    client_fk         UUID NOT NULL,
    CONSTRAINT pk_projects PRIMARY KEY (id)
);

CREATE TABLE identity.users
(
    id          UUID NOT NULL,
    name        VARCHAR(255),
    email       VARCHAR(255) UNIQUE,
    password    VARCHAR(255),
    hourly_rate DECIMAL,
    annotation  TEXT,
    occupation  VARCHAR(60),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE identity.users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE planning.clients
    ADD CONSTRAINT FK_CLIENTS_ON_USER_FK FOREIGN KEY (user_fk) REFERENCES identity.users (id);

ALTER TABLE planning.kanban_columns
    ADD CONSTRAINT FK_KANBAN_COLUMNS_ON_PROJECT_FK FOREIGN KEY (project_fk) REFERENCES planning.projects (id);

ALTER TABLE planning.kanban_tasks
    ADD CONSTRAINT FK_KANBAN_TASKS_ON_COLUMN_FK FOREIGN KEY (column_fk) REFERENCES planning.kanban_columns (id);

ALTER TABLE planning.projects
    ADD CONSTRAINT FK_PROJECTS_ON_CLIENT_FK FOREIGN KEY (client_fk) REFERENCES planning.clients (id);

ALTER TABLE planning.projects
    ADD CONSTRAINT FK_PROJECTS_ON_USER_FK FOREIGN KEY (user_fk) REFERENCES identity.users (id);

