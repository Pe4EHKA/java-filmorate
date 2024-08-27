INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES ('email1@email.com', 'login1', 'name1', '1999-09-09'),
       ('email2@email.com', 'login2', 'name2', '1999-09-09'),
       ('email3@email.com', 'login3', 'name3', '1999-09-09');

INSERT INTO FRIENDSHIPS (FROM_USER_ID, TO_USER_ID, ACCEPTED)
VALUES (2, 1, true),
       (1, 2, true),
       (2, 3, false);

MERGE INTO GENRES (ID, NAME)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO MPA (ID, NAME)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
VALUES ('film1', 'desc1', '1999-09-09', 10, 1),
       ('film2', 'desc2', '1999-09-09', 10, 2);

INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID)
VALUES (1, 1),
       (1, 2);

INSERT INTO FILM_LIKES (FILM_ID, USER_ID)
VALUES (1, 1);

