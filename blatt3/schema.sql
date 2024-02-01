CREATE TABLE Nutzer(
    EMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL CHECK (EMailAdresse REGEXP '^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$'),
    Vorname VARCHAR(100) NOT NULL CHECK (NOT glob('*[^ -~]*', Vorname) and length(trim(Vorname)) >0),
    Nachname VARCHAR(100) NOT NULL CHECK (NOT glob('*[^ -~]*', Nachname) and length(trim(Nachname)) >0),
    Password VARCHAR(8) NOT NULL CHECK(
      	NOT glob('*[^ -~]*', Password) and
    	Password not GLOB '*Q*' and 
        Password not glob '*W*' and
        Password not glob '*E*' and 
        Password not glob '*R*' AND
        Password not glob '*T*' AND
        Password not glob '*Y*' AND
        Password GLOB '*[0-9]*[0-9]*[0-9]*' and
        Password GLOB '*[A-Z]*[A-Z]*' AND
        length(Password) between 4 and 8),
    PRIMARY KEY (EMailAdresse)
);

CREATE TABLE IF NOT EXISTS Wohnort(
    ID INTEGER NOT NULL, 
    Stadt VARCHAR(100) NOT NULL CHECK (NOT glob('*[^ -~]*', Stadt) and length(trim(Stadt)) >0),
    PLZ VARCHAR(15) NOT NULL CHECK (NOT glob('*[^ -~]*', PLZ) and length(trim(PLZ)) >0),
    Strasse VARCHAR(255) NOT NULL CHECK (NOT glob('*[^ -~]*', Strasse) and length(trim(Strasse)) >0),
    Hausnummer VARCHAR(15) NOT NULL CHECK (NOT glob('*[^ -~]*', Hausnummer) and length(trim(Hausnummer)) >0),
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS Buerger(
    EMailAdresse VARCHAR(255) NOT NULL COLLATE NOCASE NOT NULL CHECK (EMailAdresse REGEXP '^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$'),
    WohnortID INTEGER NOT NULL,
    PRIMARY KEY (EMailAdresse),
    FOREIGN KEY (EMailAdresse) REFERENCES Nutzer(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (WohnortID) REFERENCES Wohnort(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Standort(
    ID INTEGER NOT NULL,
    Breitengrad DOUBLE NOT NULL CHECK (Breitengrad >= 0 AND Breitengrad <=180),
    Laengengrad DOUBLE NOT NULL CHECK (Laengengrad >= 0 AND Laengengrad <=180),
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS Pflanzentyp(
    ID INTEGER NOT NULL,
    Bezeichnung VARCHAR(100) COLLATE NOCASE NOT NULL UNIQUE CHECK (bezeichnung NOT GLOB '*[^A-Za-z]*'),
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS Pflanze(
    ID INTEGER NOT NULL,
    deutscheBezeichnung VARCHAR(100) NOT NULL CHECK (NOT glob('*[^ -~]*', deutscheBezeichnung) and length(trim(deutscheBezeichnung)) >0),
    lateinischeBezeichnung VARCHAR(100) NOT NULL CHECK (NOT glob('*[^ -~]*', lateinischeBezeichnung) and length(trim(lateinischeBezeichnung)) >0),
    NutzerEMailAdresse VARCHAR(255) NOT NULL COLLATE NOCASE CHECK (NutzerEMailAdresse REGEXP '^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$'),
    StandortID INTEGER NOT NULL,
    PflanzentypID INTEGER NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (NutzerEMailAdresse) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (StandortID) REFERENCES Standort(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (PflanzentypID) REFERENCES Pflanzentyp(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Bild(
    ID INTEGER NOT NULL,
    Bildpfad TEXT COLLATE NOCASE UNIQUE NOT NULL CHECK (NOT glob('*[^ -~]*', Bildpfad) and length(trim(Bildpfad)) >0),
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS PflanzeGespeichert_unterBild(
    PflanzeID INTEGER NOT NULL,
    BildID INTEGER NOT NULL,
    PRIMARY KEY (PflanzeID, BildID),
    FOREIGN KEY (PflanzeID) REFERENCES Pflanze(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (BildID) REFERENCES Bild(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TRIGGER Check_BilderCount
BEFORE INSERT ON PflanzeGespeichert_unterBild
BEGIN
	select
	CASE WHEN (
    	SELECT COUNT(*) FROM PflanzeGespeichert_unterBild
    	WHERE PflanzeID = NEW.PflanzeID
	) >= 5
    then RAISE(ABORT, 'Eine Pflanze kann höchstens 5 zugeordnete Bilder haben.') END;
END;

CREATE TABLE IF NOT EXISTS Gaertner(
    EMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL CHECK (EMailAdresse REGEXP '^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$'),
    PRIMARY KEY (EMailAdresse),
    FOREIGN KEY (EMailAdresse) REFERENCES Nutzer(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Pflegeart(
    ID INTEGER NOT NULL,
    Name VARCHAR(100) COLLATE NOCASE UNIQUE NOT NULL CHECK (Name NOT GLOB '*[^A-Za-z]*'),
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS Pflegemassnahme(
    ID INTEGER NOT NULL,
    Datum DATETIME NOT NULL CHECK (GLOB('[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9] [0-9][0-9]:[0-9][0-9]:[0-9][0-9]', Datum) AND strftime('%F %H:%M:%S', Datum) = Datum),
    PflegeartID INTEGER NOT NULL,
    NutzerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL CHECK (NutzerEMailAdresse REGEXP '^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$'),
    PRIMARY KEY (ID),
    FOREIGN KEY (NutzerEMailAdresse) REFERENCES Gaertner(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (PflegeartID) REFERENCES Pflegeart(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS PflanzeVersorgt_vonPflegemassnahme(
    PflanzeID INTEGER NOT NULL,
    PflegemassnahmeID INTEGER NOT NULL,
    PRIMARY KEY (PflanzeID, PflegemassnahmeID),
    FOREIGN KEY (PflanzeID) REFERENCES Pflanze(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (PflegemassnahmeID) REFERENCES Pflegemassnahme(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS BuergerNimmtteilPflegemassnahme(
    PflegemassnahmeID INTEGER NOT NULL,
    NutzerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL CHECK (NutzerEMailAdresse REGEXP '^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$'),
    PRIMARY KEY (PflegemassnahmeID, NutzerEMailAdresse),
    FOREIGN KEY (PflegemassnahmeID) REFERENCES Pflegemassnahme(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (NutzerEMailAdresse) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS setzt_voraus(
    PflegemassnahmeID1 INTEGER NOT NULL,
    PflegemassnahmeID2 INTEGER NOT NULL,
    PRIMARY KEY (PflegemassnahmeID1),
    FOREIGN KEY (PflegemassnahmeID1) REFERENCES Pflegemassnahme(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (PflegemassnahmeID2) REFERENCES Pflegemassnahme(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TRIGGER Check_Teilnahme
BEFORE INSERT ON BuergerNimmtteilPflegemassnahme
BEGIN
	select
	CASE WHEN (
    	NEW.pflegemassnahmeid IN (
    	SELECT pflegemassnahmeid1 from setzt_voraus
      	WHERE pflegemassnahmeid1 = NEW.pflegemassnahmeid
	))
    then RAISE(ABORT, 'Bürger können nur an Pflegemaßnahmen teilnehmen, welche keine anderen Pflegemaßnahmen voraussetzen') END;
END;

CREATE TABLE IF NOT EXISTS PflanzeGepflanzt_vonGaertner(
    PflanzeID INTEGER NOT NULL,
    NutzerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL CHECK (NutzerEMailAdresse REGEXP '^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$'),
    Pflanzdatum DATE CHECK (GLOB('[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]', Pflanzdatum) AND strftime('%Y-%m-%d', Pflanzdatum) = Pflanzdatum),
    PRIMARY KEY (PflanzeID),
    FOREIGN KEY (PflanzeID) REFERENCES Pflanze(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (NutzerEMailAdresse) REFERENCES Gaertner(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Spezialisierung(
    ID INTEGER NOT NULL,
    Bezeichnung VARCHAR(100) COLLATE NOCASE NOT NULL UNIQUE CHECK (bezeichnung NOT GLOB '*[^A-Za-z]*'),
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS GaertnerHatSpezialisierung(
    NutzerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL CHECK (NutzerEMailAdresse REGEXP '^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$'),
    SpezialisierungID INTEGER NOT NULL,
    PRIMARY KEY (NutzerEMailAdresse, SpezialisierungID),
    FOREIGN KEY (SpezialisierungID) REFERENCES Spezialisierung(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (NutzerEMailAdresse) REFERENCES Gaertner(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Pflegeprotokoll(
    ID INTEGER NOT NULL,
    NutzerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL CHECK (NutzerEMailAdresse REGEXP '^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$'),
    Erfolg BOOLEAN NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (NutzerEMailAdresse) REFERENCES Gaertner(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS GaertnerBewertetPflegemassnahme(
    PflegemassnahmeID INTEGER NOT NULL,
    NutzerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL CHECK (NutzerEMailAdresse REGEXP '^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+$'),
    Skala INTEGER CHECK (Skala >= 1 AND Skala <= 5),
    PRIMARY KEY (PflegemassnahmeID, NutzerEMailAdresse),
    FOREIGN KEY (PflegemassnahmeID) REFERENCES Pflegemassnahme(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (NutzerEMailAdresse) REFERENCES Gaertner(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TRIGGER check_bewertung
BEFORE INSERT ON GaertnerBewertetPflegemassnahme
BEGIN
    SELECT
    case when NEW.NutzerEMailAdresse not in (
        SELECT NutzerEMailAdresse
        FROM Pflegemassnahme
        WHERE ID = NEW.PflegemassnahmeID)
    then RAISE(ABORT, 'Ein Gärtner kann nur Pflegemaßnahmen bewerten, die auch von diesem Gärtner betreut werden.') ENd;
END;

CREATE TABLE IF NOT EXISTS PflegeprotokollEnthaeltPflegemassnahme(
    PflegemassnahmeID INTEGER NOT NULL,
    PflegeprotokollID INTEGER NOT NULL,
    PRIMARY KEY (PflegemassnahmeID, PflegeprotokollID),
    FOREIGN KEY (PflegemassnahmeID) REFERENCES Pflegemassnahme(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (PflegeprotokollID) REFERENCES Pflegeprotokoll(ID) ON UPDATE CASCADE ON DELETE CASCADE
);








