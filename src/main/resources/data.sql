INSERT INTO ARTICOLI  (codArt, titolo, descrizione, prezzo, data_stampa) VALUES (100000, 'Animali', 'Le magiche ..', 20, '2020-01-05');
INSERT INTO ARTICOLI  (codArt, titolo, descrizione, prezzo, data_stampa) VALUES (100002, 'Cani', 'Fantasia ..', 50, '2018-04-01');

INSERT INTO BARCODE  (barcode, id_tipo_art, codArt) VALUES (10000000, 'Libro', 100000);
INSERT INTO BARCODE  (barcode, id_tipo_art, codArt) VALUES (10000002, 'Libro', 100002);

--INSERT INTO USER  (id, email, username, roles, password) VALUES (1, 'marco@libero.it', 'Marco', 'USER', 'user');
--INSERT INTO USER  (id, email, username, roles, password) VALUES (2, 'antonio@libero.it', 'Antonio', 'ADMIN', 'admin');
