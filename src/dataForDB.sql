-- 5 clienti inregistrati in Postman
-- Variabile pentru clienti
DECLARE @clientId1 AS INT = 2;
DECLARE @clientId2 AS INT = 3;
DECLARE @clientId3 AS INT = 4;
DECLARE @clientId4 AS INT = 5;
DECLARE @clientId5 AS INT = 6;

-- Stergem datele din tabele
DELETE FROM address;
DELETE FROM inventory;
DELETE FROM web_order;
DELETE FROM web_order_quantity;
DELETE FROM wine;



-- Inseram vinuri
INSERT INTO wine ([name], type, description, price) VALUES ('Merlot', 'Rosu', 'Merlot este un vin cu un corp mediu, aciditate moderată, taninuri fine.', 26.50);
INSERT INTO wine ([name], type, description, price) VALUES ('Chardonnay', 'Alb', 'In esenta, vinul Chardonnay este un vin alb corpolent, onctuos, cu arome bogate de fructe.', 44.00);
INSERT INTO wine ([name], type, description, price) VALUES ('Sauvignon Blanc', 'Alb', 'Sec, demisec sau dulce, Sauvignon blanc este un vin cu multa viata, placut, acid si aromat.', 49.43);
INSERT INTO wine ([name], type, description, price) VALUES ('Pinot Noir', 'Rosu', 'Cand este tanar, Pinot Noir ofera simple caracteristici de cirese, prune si capsuni. Pe masura ce devine mai matur, caracteristicile sale devin mai complexe, incluzand stafide, fan, tutun, piele, ciuperci sau piper negru.', 38.60);
INSERT INTO wine ([name], type, description, price) VALUES ('Decoy', 'Rose', 'Vinul este sulce cu arome fructate.', 45.34);
INSERT INTO wine ([name], type, description, price) VALUES ('Shiraz', 'Rosu', 'Shiraz este un celebru vin rosu australian, cu un continut ridicat de alcool, dar si de antioxidanti.', 49.77);
INSERT INTO wine ([name], type, description, price) VALUES ('Lambrusco', 'Rosu', 'Vin rosu spumant cu perlaj fin si note intrigante de fructe rosii.', 27.90);
INSERT INTO wine ([name], type, description, price) VALUES ('Cabernet Sauvignion', 'Rosu', 'Vinul este inchis la culoare, cu o nuanta de rosu-purpuriu, data de cantitatea impresionanta de antioxidanti pe care ii contine. Este opac si are gust puternic si acid.', 27.00);
INSERT INTO wine ([name], type, description, price) VALUES ('Moscato', 'Alb', 'Moscato are gust fructat, cu aroma puternica de grepfruit si e usor de recunoscut printre degustatori.', 23.00);
INSERT INTO wine ([name], type, description, price) VALUES ('Riesling', 'Alb', 'Riesling este un vin aromat, fructat, care isi are originea in Germania, insa la noi in tara este mai cunoscut Rieslingul Italian, un vin mai degraba sec si sobru.', 45.00);
INSERT INTO wine ([name], type, description, price) VALUES ('Rosadio', 'Rose', 'Este un vin aromatic intens, dar elegant si subtil. Arome de anason si ierburi, insa vom simti si note de capsuni si coacaze negre.', 30.00);
INSERT INTO wine ([name], type, description, price) VALUES ('Pinot Grigio', 'Alb', 'Vinul este sec, cu gust puternic si acid, cu arome fructate.', 27.00);

-- Variabile pentru fiecare vin
DECLARE @wine1 INT, @wine2 INT, @wine3 INT, @wine4 INT, @wine5 INT, @wine6 AS INT;
DECLARE @wine7 INT, @wine8 INT, @wine9 INT, @wine10 INT, @wine11 INT, @wine12 AS INT;

SELECT @wine1 = id FROM wine WHERE [name] = 'Merlot';
SELECT @wine2 = id FROM wine WHERE [name] = 'Chardonnay';
SELECT @wine3 = id FROM wine WHERE [name] = 'Sauvignon Blanc';
SELECT @wine4 = id FROM wine WHERE [name] = 'Pinot Noir';
SELECT @wine5 = id FROM wine WHERE [name] = 'Decoy';
SELECT @wine6 = id FROM wine WHERE [name] = 'Shiraz';
SELECT @wine7 = id FROM wine WHERE [name] = 'Lambrusco';
SELECT @wine8 = id FROM wine WHERE [name] = 'Cabernet Sauvignion';
SELECT @wine9 = id FROM wine WHERE [name] = 'Moscato';
SELECT @wine10 = id FROM wine WHERE [name] = 'Riesling';
SELECT @wine11 = id FROM wine WHERE [name] = 'Rosadio';
SELECT @wine12 = id FROM wine WHERE [name] = 'Pinot Grigio';

-- inseram nr. de vinuri din stoc
INSERT INTO inventory (wine_id, quantity) VALUES (@wine1, 10);
INSERT INTO inventory (wine_id, quantity) VALUES (@wine2, 23);
INSERT INTO inventory (wine_id, quantity) VALUES (@wine3, 45);
INSERT INTO inventory (wine_id, quantity) VALUES (@wine4, 12);
INSERT INTO inventory (wine_id, quantity) VALUES (@wine5, 5);
INSERT INTO inventory (wine_id, quantity) VALUES (@wine6, 34);
INSERT INTO inventory (wine_id, quantity) VALUES (@wine7, 68);
INSERT INTO inventory (wine_id, quantity) VALUES (@wine8, 100);
INSERT INTO inventory (wine_id, quantity) VALUES (@wine9, 123);
INSERT INTO inventory (wine_id, quantity) VALUES (@wine10, 54);
INSERT INTO inventory (wine_id, quantity) VALUES (@wine11, 23);
INSERT INTO inventory (wine_id, quantity) VALUES (@wine12, 46);

-- Adrese pentru clienti
INSERT INTO address (address_line_1, city, country, client_id) VALUES ('Str. Toamnei, nr. 13', 'Iasi', 'Romania', @clientId1);
INSERT INTO address (address_line_1, city, country, client_id) VALUES ('Str. Eroilor, nr. 21', 'Cluj', 'Romania', @clientId2);
INSERT INTO address (address_line_1, city, country, client_id) VALUES ('Str. Soarelui, nr. 10', 'Constanta', 'Romania', @clientId3);
INSERT INTO address (address_line_1, city, country, client_id) VALUES ('Str. Iuliu Maniu, nr. 9B', 'Bucuresti', 'Romania', @clientId4);
INSERT INTO address (address_line_1, city, country, client_id) VALUES ('Str. Pacii, nr. 6', 'Targu Jiu', 'Romania', @clientId5);

-- Variabile pentru adresele existente
DECLARE @address1 INT, @address2 INT, @address3 INT, @address4 INT, @address5 INT;

SELECT TOP 1 @address1 = id FROM address WHERE client_id = @clientId1 ORDER BY id DESC;
SELECT TOP 1 @address2 = id FROM address WHERE client_id = @clientId2 ORDER BY id DESC;
SELECT TOP 1 @address3 = id FROM address WHERE client_id = @clientId3 ORDER BY id DESC;
SELECT TOP 1 @address4 = id FROM address WHERE client_id = @clientId4 ORDER BY id DESC;
SELECT TOP 1 @address5 = id FROM address WHERE client_id = @clientId5 ORDER BY id DESC;

-- Inseram comenzi pentru fiecare client
INSERT INTO web_order (address_id, client_id) VALUES (@address1, @clientId1);
INSERT INTO web_order (address_id, client_id) VALUES (@address1, @clientId1);
INSERT INTO web_order (address_id, client_id) VALUES (@address1, @clientId1);

INSERT INTO web_order (address_id, client_id) VALUES (@address2, @clientId2);
INSERT INTO web_order (address_id, client_id) VALUES (@address2, @clientId2);

INSERT INTO web_order (address_id, client_id) VALUES (@address3, @clientId3);
INSERT INTO web_order (address_id, client_id) VALUES (@address3, @clientId3);
INSERT INTO web_order (address_id, client_id) VALUES (@address3, @clientId3);

INSERT INTO web_order (address_id, client_id) VALUES (@address4, @clientId4);
INSERT INTO web_order (address_id, client_id) VALUES (@address4, @clientId4);

INSERT INTO web_order (address_id, client_id) VALUES (@address5, @clientId5);
INSERT INTO web_order (address_id, client_id) VALUES (@address5, @clientId5);
INSERT INTO web_order (address_id, client_id) VALUES (@address5, @clientId5);
INSERT INTO web_order (address_id, client_id) VALUES (@address5, @clientId5);

-- Variabile pentru comenzi
DECLARE @order1 INT, @order2 INT, @order3 INT, @order4 INT, @order5 INT;
DECLARE @order6 INT, @order7 INT, @order8 INT, @order9 INT, @order10 INT;
DECLARE @order11 INT, @order12 INT, @order13 INT, @order14 INT;
-- Comenzile primului client
SELECT TOP 1 @order1 = id FROM web_order WHERE address_id = @address1 AND client_id = @clientId1 ORDER BY id DESC
SELECT @order2 = id FROM web_order WHERE address_id = @address1 AND client_id = @clientId1 ORDER BY id DESC OFFSET 1 ROW FETCH FIRST 1 ROW ONLY
SELECT  @order3 = id FROM web_order WHERE address_id = @address1 AND client_id = @clientId1 ORDER BY id DESC OFFSET 2 ROW FETCH FIRST 1 ROW ONLY
-- Comenzile celui de-al doilea client
SELECT TOP 1 @order4 = id FROM web_order WHERE address_id = @address2 AND client_id = @clientId2 ORDER BY id DESC
SELECT @order5 = id FROM web_order WHERE address_id = @address2 AND client_id = @clientId2 ORDER BY id DESC OFFSET 1 ROW FETCH FIRST 1 ROW ONLY
-- Comenzi al 3-lea client
SELECT TOP 1 @order6 = id FROM web_order WHERE address_id = @address3 AND client_id = @clientId3 ORDER BY id DESC
SELECT @order7 = id FROM web_order WHERE address_id = @address3 AND client_id = @clientId3 ORDER BY id DESC OFFSET 1 ROW FETCH FIRST 1 ROW ONLY
SELECT  @order8 = id FROM web_order WHERE address_id = @address3 AND client_id = @clientId3 ORDER BY id DESC OFFSET 2 ROW FETCH FIRST 1 ROW ONLY
-- Comenzi al 4-lea client
SELECT TOP 1 @order9 = id FROM web_order WHERE address_id = @address4 AND client_id = @clientId4 ORDER BY id DESC
SELECT @order10 = id FROM web_order WHERE address_id = @address4 AND client_id = @clientId4 ORDER BY id DESC OFFSET 1 ROW FETCH FIRST 1 ROW ONLY
--Comenzi al 5-lea client
SELECT TOP 1 @order11 = id FROM web_order WHERE address_id = @address5 AND client_id = @clientId5 ORDER BY id DESC
SELECT @order12 = id FROM web_order WHERE address_id = @address5 AND client_id = @clientId5 ORDER BY id DESC OFFSET 1 ROW FETCH FIRST 1 ROW ONLY
SELECT  @order13 = id FROM web_order WHERE address_id = @address5 AND client_id = @clientId5 ORDER BY id DESC OFFSET 2 ROW FETCH FIRST 1 ROW ONLY
SELECT  @order14 = id FROM web_order WHERE address_id = @address5 AND client_id = @clientId5 ORDER BY id DESC OFFSET 3 ROW FETCH FIRST 1 ROW ONLY

-- Cantitatea de diferite vinuri din comenzi
-- Comenzi client 1
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order1, @wine1, 5);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order1, @wine5, 10);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order2, @wine8, 20);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order2, @wine10, 10);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order2, @wine12, 3);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order3, @wine5, 5);
-- Comenzi client 2
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order4, @wine9, 7);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order4, @wine8, 10);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order5, @wine3, 1);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order5, @wine2, 6);
-- Comenzi client 3
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order6, @wine6, 1);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order7, @wine5, 5);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order8, @wine6, 3);
-- Comenzi client 4
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order9, @wine1, 10);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order9, @wine4, 6);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order9, @wine3, 8);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order10, @wine12, 5);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order10, @wine11, 4);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order10, @wine8, 2);
-- Comenzi client 5
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order11, @wine12, 2);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order12, @wine4, 4);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order13, @wine3, 10);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order14, @wine2, 10);
INSERT INTO web_order_quantity (web_order_id, wine_id, quantity) VALUES (@order14, @wine4, 10);

-- Error executing DDL "alter table client add is_email_verified bit not null" via JDBC
-- Pentru a adauga is_email_verified in tabelul client, execut comanda de mai jos in SQLServer
ALTER TABLE client ADD is_email_verified bit NOT NULL DEFAULT 0