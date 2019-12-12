CREATE SCHEMA IF NOT EXISTS "delivery";
SET search_path TO "delivery", public;

INSERT INTO delivery.product(id, name, product_id, price, weight) VALUES(1, 'Product one', 3423423, 22000, '5');
INSERT INTO delivery.product(id, name, product_id, price, weight) VALUES(2, 'Product one', 34234234, 62000, '8');
INSERT INTO delivery.product(id, name, product_id, price, weight) VALUES(3, 'Product one', 3423423234, 2000, '2');
INSERT INTO delivery.product(id, name, product_id, price, weight) VALUES(4, 'Product one', 34234, 5000, '7');
INSERT INTO delivery.product(id, name, product_id, price, weight) VALUES(5, 'Product one', 123134, 4500, '2');