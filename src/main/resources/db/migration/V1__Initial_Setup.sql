DROP TABLE IF EXISTS "books";
CREATE TABLE "books" (
  "id" int8 NOT NULL PRIMARY KEY,
  "author" varchar(255),
  "title" varchar(255)
)
;