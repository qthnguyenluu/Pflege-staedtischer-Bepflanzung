# Blatt 3

Implementierung der Datenbank im vorgegebenen SQL-Dialekt

## Kritische Entscheidungen

- Jedes nicht optionale Attribut, dessen dazugehörige Spalte den Datentyp VARCHAR oder TEXT besitzt, darf im Wertebereich nicht die leere Zeichenkette enthalten. Zum Beispiel ist der Wert (' ') nicht erlaubt und wurde mittels length(trim) auf > 0 geprüft. Außerdem darf der Wertebereich nur Zeichenketten enthalten, die nur darstellbare ASCII-Zeichen (Code 20-7E in der ASCII-Tabelle) enthalten, was mittels glob für Zeichen von "space" zu "~" überprüft wurde.

- Jede E-Mail-Adresse ist im Format X@Y.Z, wobei X, Y und Z nicht leere Zeichenketten sind, die nur aus Buchstaben des lateinischen Alphabets bestehen. Außerdem dürfen X und Y Ziffern enthalten, was mittels REGEXP durchgeführt wurde.

- EmailAdresse (primary key), Pflanzentyp.Bezeichnung, Bild.BildPfad, Pflegeart.Name, Spezialisierung.Bezeichnung sind UNIQUE und Case-insensitive.

- Alle Fremdschlüssel wurden kaskadierend bei UPDATE und DELETE gesetzt, was Sinn macht

- Die Anforderung bezüglich des Passworts, also kein Konsonant aus der Menge {Q, W, E, R, T, Y}, bezieht sich nur auf Großbuchstaben, da das Passwort für mich case-sensitive sinnvoller wäre.

- Wohnort.PLZ und Hausnummer sind vom Typ VARCHAR, weil sie auch Buchstaben enthalten dürfen.

- Der Wertebereich der Bewertungsskala ist {1, 2, 3, 4, 5}.

- Pflanzentyp.Bezeichnung, Pflegeart.Name und Spezialisierung.Bezeichnung bestehen nur aus Zeichen des lateinischen Alphabets.

- PflanzeGepflanzt_vonGaertner.Pflanzdatum ist ein optionales Attribut und kann daher den Wert NULL enthalten.

- Pflegeprotokoll.Erfolg ist vom Typ Boolean, mit den möglichen Werten true und false.

- Die Aussagen: "Ein Gärtner kann nur Pflegemaßnahmen bewerten, die auch von diesem Gärtner betreut werden. Eine Pflanze kann höchstens 5 zugeordnete Bilder haben. Bürger können nur an Pflegemaßnahmen teilnehmen, welche keine anderen Pflegemaßnahmen voraussetzen." wurden mittels TRIGGER implementiert.

- Ein Beispiel für den Trigger check_teilnahme: In der Tabelle setzt_voraus haben wir die Werte (2, 1), (3, 1), (4, 3). Das bedeutet, dass Pflegemaßnahme 1 eine Voraussetzung für Pflegemaßnahme 2 und 3 ist, und 3 ist eine Voraussetzung für 4 (Jede Pflegemaßnahme kann Voraussetzung für beliebig viele andere Pflegemaßnahmen sein). Dies bedeutet, dass Bürger NICHT an den Pflegemaßnahmen 2, 3 und 4 teilnehmen können, da die Maßnahmen 2 und 3 die Maßnahme 1 voraussetzen und die Maßnahme 4 die Maßnahme 3 voraussetzt.

- Pflanzen, die wildwachsend sind, wurden nicht von Gärtner gepflanzt, deren PflanzenID nicht in der Tabelle PflanzeGepflanzt_vonGaertner existieren. 

- Es können mehrere fleißigste Bürger ausgegeben werden.
