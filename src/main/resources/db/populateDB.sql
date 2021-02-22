DELETE
FROM user_roles;
DELETE
FROM users;
DELETE
FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);


INSERT INTO meals (DATE_TIME, DESCRIPTION, CALORIES, USER_ID)
VALUES ('2020-06-20 10:00:00', 'завтрак', 500, 100000),
       ('2020-06-20 13:00:00', 'обед', 1000, 100000),
       ('2020-06-20 20:00:00', 'ужин', 500, 100000),
       ('2020-06-21 10:00:00', 'завтрак', 500, 100000),
       ('2020-06-21 13:00:00', 'обед', 1000, 100000),
       ('2020-06-21 20:00:00', 'ужин', 500, 100000),
       ('2020-06-22 14:00:00', 'Админ ланч', 510, 100001),
       ('2020-06-22 21:00:00', 'Админ ужин', 1500, 100001);
