DROP TABLE IF EXISTS "books";
CREATE SEQUENCE book_id_seq START 1;
CREATE TABLE "books" (
  "id" int8 NOT NULL PRIMARY KEY DEFAULT nextval('book_id_seq'),
  "author" varchar(255),
  "title" varchar(255)
);
