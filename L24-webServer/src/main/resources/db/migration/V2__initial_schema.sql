
INSERT INTO client (id, name, login, password, role_client)
values (nextval('client_SEQ'), 'Ivan', 'ivan_login', '123456', 'USER'),
       (nextval('client_SEQ'), 'Petr', 'petr_login', 'password1', 'USER'),
       (nextval('client_SEQ'), 'Svetlana', 'svetlana_login', 'qwerty', 'USER'),
       (nextval('client_SEQ'), 'Olga', 'olga_login', 'letmein', 'USER'),
       (nextval('client_SEQ'), 'Dmitry', 'dmitry_login', '123qwe', 'USER'),
       (nextval('client_SEQ'), 'Alexey', 'alexey_login', 'password123', 'USER'),
       (nextval('client_SEQ'), 'Maria', 'maria_login', 'admin', 'USER'),
       (nextval('client_SEQ'), 'Sergey', 'sergey_login', 'welcome', 'USER'),
       (nextval('client_SEQ'), 'Elena', 'elena_login', 'passw0rd', 'USER'),
       (nextval('client_SEQ'), 'Nikolay', 'nikolay_login', '12345678', 'USER'),
       (nextval('client_SEQ'), 'Admin', 'admin', '123admin', 'ADMIN');
