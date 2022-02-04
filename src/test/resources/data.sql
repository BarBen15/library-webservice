INSERT INTO 
	ARTICOLI (codart, descrizione, prezzo, data_stampa)
VALUES
  	(2000301, 'PINOCCHIO', 65, '2019-05-14'),
  	(12346, 'LA CASA DI CARTA', 48, '2019-05-14');

INSERT INTO
	BARCODE (barcode, id_tipo_art, codart)
VALUES
  	(800850, 'LIBRO', 12346),
  	(800849, 'CD', 2000301);