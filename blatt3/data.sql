INSERT INTO Nutzer values
	('jdoedoe@ost.de','Marcus John','Doe','1AB11a'),
  	('d12345oe@dhb.de','Dimmy-Cho','San','132sABC'),
  	('abc123@email.com','Mike','Choe','BV9090'),
  	('sinsin12@fir.com','Sin','Kim','KI13d2'),
    ('abc@email.com','Johnny','Pot','000qqUZ'),
    ('mann987@exp.com','Anna','Holt','999HOss'),
    ('johnd@hotmail.com','John','Dodey','POA777B'),
    ('hoe98@ex2.com','Hoe','Hong','13d2HM'),
    ('jjun12@ex1.com','Jun','Jun','113dAB');

INSERT INTO Wohnort VALUES
	(1, 'Duesseldorf', '40213', 'Neustr.', '123a'),
    (2, 'Duesseldorf', '40235', 'Blumstr.', '10'),
    (3, 'Duesseldorf', '40213', 'Neustr.', '12'),
    (4, 'Ottawa', 'K1A 0A1', 'Highland Park', '12-14');

INSERT INTO Buerger VALUES
	('jdoedoe@ost.de', 3),
    ('d12345oe@dhb.de', 4),
    ('abc@email.com',1),
    ('abc123@email.com',1),
    ('sinsin12@fir.com',2);

INSERT INTO Standort VALUES
	(1, 12.34, 90.00),
    (2, 10.10, 60.12),
    (3, 10.10, 120.111);

INSERT INTO Pflanzentyp VALUES 
	(1, 'Baum'),
    (2, 'Strauch'),
    (3, 'Blume');

INSERT INTO Pflanze VALUES
	(1, 'Orchidee', 'Orchidaceae', 'abc@email.com', 1, 3),
    (2, 'Feige', 'Ficus', 'abc@email.com', 2, 1),
    (3, 'Apfel', 'Malus prunifolia', 'abc@email.com', 1, 1),
    (4, 'Orchidee', 'Orchidaceae', 'abc123@email.com', 1, 3),
    (5, 'Feige', 'Ficus', 'abc123@email.com', 2, 1),
    (6, 'Apfel', 'Malus prunifolia', 'abc123@email.com', 1, 1),
    (7, 'Apfel', 'Malus prunifolia', 'd12345oe@dhb.de', 2, 1),
    (8, 'Apfel', 'Malus prunifolia', 'jdoedoe@ost.de', 3, 1),
    (9, 'Feige', 'Ficus', 'jdoedoe@ost.de', 2, 1);

INSERT INTO Bild VALUES
	(1, 'C:\Users\Desktop\Photo'),
    (2, 'c:\Users\Desktop\Photo_2'),
    (3, 'd:\Users\Desktop\Photo_3'),
    (4, 'e:\Users\Desktop\Plant_2'),
    (5, 'c:\Users\Desktop\Plant_1'),
    (6, 'c:\Users\Album\~Photo_2');

INSERT INTO PflanzeGespeichert_unterBild VALUES
	(1,1),
    (1,2),
    (1,3),
    (1,4),
    (1,5),
    (3,1),
    (7,6),
    (7,1);

INSERT into Gaertner VALUES
	('johnd@hotmail.com'),
    ('jjun12@ex1.com'),
    ('hoe98@ex2.com'),
    ('mann987@exp.com');

INsert into Pflegeart VALUES
	(1, 'Bewaesserung'),
    (2, 'Duengung');

insert into Pflegemassnahme VALUES
	(1, '2022-12-12 07:00:00', 1, 'johnd@hotmail.com'),
    (2, '2023-01-11 13:00:00', 1, 'johnd@hotmail.com'),
    (3, '2023-02-09 08:30:00', 1, 'johnd@hotmail.com'),
    (4, '2023-02-12 07:00:00', 1, 'hoe98@ex2.com'),
    (5, '2023-03-30 10:30:00', 1, 'hoe98@ex2.com'),
    (6, '2022-11-05 10:15:00', 2, 'jjun12@ex1.com');

insert into PflanzeVersorgt_vonPflegemassnahme VALUES
	(1, 1),
    (1, 2),
    (3, 1),
    (3, 5),
    (4, 1);


INSERT INTo setzt_voraus VALUES 
	(2, 1),
    (3, 1),
    (4, 3);

insert into BuergerNimmtteilPflegemassnahme values 
	(1, 'abc@email.com'),
    (1, 'd12345oe@dhb.de'),
    (1, 'jdoedoe@ost.de'),
    (5, 'abc123@email.com'),
    (6, 'd12345oe@dhb.de'),
    (1, 'abc123@email.com');

insert into PflanzeGepflanzt_vonGaertner VALUES
	(1, 'johnd@hotmail.com', NULL),
    (2, 'johnd@hotmail.com', '2022-12-01'),
    (3, 'hoe98@ex2.com', '2022-12-02'),
    (5, 'jjun12@ex1.com', '2022-12-03');

insert into Spezialisierung VALUES
	(1,'Blumenpflege'),
    (2,'Obstbau'),
    (3, 'Baumpflege');

INSERT into GaertnerHatSpezialisierung VALUES
	('johnd@hotmail.com', 1),
    ('johnd@hotmail.com', 2),
    ('hoe98@ex2.com', 3),
    ('jjun12@ex1.com', 2),
    ('mann987@exp.com', 2);

insert into Pflegeprotokoll VALUES
	(1, 'johnd@hotmail.com', true),
    (2, 'jjun12@ex1.com', false),
    (3, 'jjun12@ex1.com', true),
    (4, 'hoe98@ex2.com', true);


insert into GaertnerBewertetPflegemassnahme VALUES 
	(1, 'johnd@hotmail.com', 5),
    (3, 'johnd@hotmail.com', 5),
    (4, 'hoe98@ex2.com', 3),
    (5, 'hoe98@ex2.com', 3);

insert into PflegeprotokollEnthaeltPflegemassnahme VALUES
	(1, 4),
    (1, 3),
    (2, 1),
    (6, 3),
    (3, 3);