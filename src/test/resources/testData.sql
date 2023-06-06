-- Junit test data

DELETE FROM client;
DELETE FROM address;
DELETE FROM inventory;
DELETE FROM web_order;
DELETE FROM web_order_quantity;
DELETE FROM wine;

INSERT INTO client (email, first_name, last_name, password, username, is_email_verified) VALUES ('ClientA@junit.com', 'ClientA-FirstName', 'ClientA-LastName', '$2a$10$hBn5gu6cGelJNiE6DDsaBOmZgyumCSzVwrOK/37FWgJ6aLIdZSSI2', 'ClientA', true);
INSERT INTO client (email, first_name, last_name, password, username, is_email_verified) VALUES ('ClientB@junit.com', 'ClientB-FirstName', 'ClientB-LastName', '$2a$10$TlYbg57fqOy/1LJjispkjuSIvFJXbh3fy0J9fvHnCpuntZOITAjVG', 'ClientB', false);
INSERT INTO client (email, first_name, last_name, password, username, is_email_verified) VALUES ('ClientC@junit.com', 'ClientC-FirstName', 'ClientC-LastName', '$2a$10$SYiYAIW80gDh39jwSaPyiuKGuhrLi7xTUjocL..NOx/1COWe5P03.', 'ClientC', false);

INSERT INTO wine (name, type, description, price) VALUES ('Wine1', 'Color1', 'Description1', 40.50);
INSERT INTO wine (name, type, description, price) VALUES ('Wine2', 'Color2', 'Description2', 26.00);
INSERT INTO wine (name, type, description, price) VALUES ('Wine3', 'Color3', 'Description3', 32.45);
INSERT INTO wine (name, type, description, price) VALUES ('Wine4', 'Color4', 'Description4', 23.50);
INSERT INTO wine (name, type, description, price) VALUES ('Wine5', 'Color5', 'Description5', 30.78);

INSERT INTO inventory (wine_id, quantity) VALUES (1, 10);
INSERT INTO inventory (wine_id, quantity) VALUES (2, 23);
INSERT INTO inventory (wine_id, quantity) VALUES (3, 45);
INSERT INTO inventory (wine_id, quantity) VALUES (4, 12);
INSERT INTO inventory (wine_id, quantity) VALUES (5, 5);

INSERT INTO address (address_line_1, city, country, client_id) VALUES ('Adresa1', 'Iasi', 'Romania', 1);
INSERT INTO address (address_line_1, city, country, client_id) VALUES ('Adresa2', 'Cluj', 'Romania', 2);
INSERT INTO address (address_line_1, city, country, client_id) VALUES ('Adresa3', 'Constanta', 'Romania', 3);

INSERT INTO web_order (address_id, client_id) VALUES (1, 1);
INSERT INTO web_order (address_id, client_id) VALUES (1, 1);
INSERT INTO web_order (address_id, client_id) VALUES (3, 3);
INSERT INTO web_order (address_id, client_id) VALUES (2, 2);
INSERT INTO web_order (address_id, client_id) VALUES (2, 2);

INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (1, 1, 5);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (1, 5, 10);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (2, 3, 20);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (2, 2, 10);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (2, 4, 3);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (3, 5, 5);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (4, 2, 7);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (4, 4, 10);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (5, 3, 1);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (5, 2, 6);



