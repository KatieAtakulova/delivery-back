CREATE SCHEMA IF NOT EXISTS "delivery";
SET search_path TO "delivery", public;

CREATE TABLE IF NOT EXISTS "user"
(
    "id"           SERIAL  NOT NULL UNIQUE,
    "full_name"    varchar NOT NULL,
    "phone_number" varchar NOT NULL,
    "password"     varchar NOT NULL,
    "login"        varchar NOT NULL,
    "role"         varchar NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );

------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS "order"
(
    "id"            SERIAL    NOT NULL UNIQUE,
    "full_name"     varchar   NOT NULL,
    "address"       varchar   NOT NULL,
    "phone_number"  varchar   NOT NULL,
    "payment_type"  varchar   NOT NULL,
    "delivery_type" varchar   NOT NULL,
    "full_price"    varchar   NOT NULL,
    "status"        varchar   NOT NULL,
    "state"         varchar   NOT NULL,
    "date"          TIMESTAMP NOT NULL,
    "user_fk"       int8      NOT NULL,
    CONSTRAINT order_pk PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );

ALTER TABLE "order"
    ADD CONSTRAINT "order_fk0" FOREIGN KEY ("user_fk") REFERENCES "user" ("id");

------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS "product"
(
    "id"         SERIAL           NOT NULL UNIQUE,
    "name"       varchar          NOT NULL,
    "product_id" int8             NOT NULL,
    "price"      double precision NOT NULL,
    "weight"     varchar          NOT NULL,
    CONSTRAINT product_pk PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );

------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS "order_product"
(
    "id"         SERIAL NOT NULL UNIQUE,
    "order_id"   int8   NOT NULL,
    "product_id" int8   NOT NULL,
    CONSTRAINT order_product_pk PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );

ALTER TABLE "order_product"
    ADD CONSTRAINT "order_products_fk0" FOREIGN KEY ("order_id") REFERENCES "order" ("id");
ALTER TABLE "order_product"
    ADD CONSTRAINT "order_products_fk1" FOREIGN KEY ("product_id") REFERENCES "product" ("id");


------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS "comment"
(
    "id"       SERIAL  NOT NULL UNIQUE,
    "text"     varchar NOT NULL,
    "rating"   int4    NOT NULL,
    CONSTRAINT comment_pk PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );

------------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS "excitement"
(
    "id"       SERIAL  NOT NULL UNIQUE,
    "text"     varchar NOT NULL,
    "address"  varchar NOT NULL,
    "order_fk" int8    NOT NULL,
    CONSTRAINT excitement_pk PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );

ALTER TABLE "excitement"
    ADD CONSTRAINT "excitement_fk0" FOREIGN KEY ("order_fk") REFERENCES "order" ("id");

