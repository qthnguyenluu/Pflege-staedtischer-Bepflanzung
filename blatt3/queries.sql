SELECT * from Pflanze p 
WHERE p.ID not IN (
    SELECT PflanzeID From PflanzeGepflanzt_vonGaertner pg
    WHERE p.ID = pg.PflanzeID)
;

SELECT *
FROM Buerger
WHERE (
    SELECT COUNT(NutzerEMailAdresse) as Times
    FROM BuergerNimmtteilPflegemassnahme n
    WHERE Buerger.EMailAdresse = n.NutzerEMailAdresse
    GROUP BY n.NutzerEMailAdresse
) = (
    SELECT MAX(a.Times)
    FROM (
        SELECT COUNT(NutzerEMailAdresse) as Times
        FROM BuergerNimmtteilPflegemassnahme n
        JOIN Buerger b ON b.EMailAdresse = n.NutzerEMailAdresse
        GROUP BY n.NutzerEMailAdresse
    ) a
);

SELECT * from Pflegemassnahme p 
WHERE p.Datum >= '2022-12-12' And p.Datum <= '2023-02-28'
