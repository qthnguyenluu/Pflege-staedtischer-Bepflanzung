# Blatt 1

Erstellen eines ER-Modells der Datenbank

## Kritische Entscheidungen

- Das Pflanzdatum wurde als optionales Attribut dargestellt.
- Die Kardinalität auf der Seite der Pflanze in der GEPFLANZT VON-Relation ist [0,1]. Ich gehe davon aus, dass die Pflanze entweder von genau einem Gärtner gepflanzt wurde oder wildwachsend ist. "Wildwachsend" bedeutet, dass die Pflanze nicht von Menschen gepflanzt wurde, daher entspricht der Mindestwert 0 der Kardinalität.
- Für die Entitäten Pflanzentyp, Pflegeart und Spezialisierung wurden Attribute wie Bezeichnung bzw. Name hinzugefügt, welche später als Spalten mit dem Datentyp String in den Tabellen gespeichert werden, wenn sie in der Datenbank abgelegt werden. Mir ist in Datenbank 1 schon einmal passiert, für die Entität "Kommentar" ohne Attribut Punktabzug zu bekommen. Es wurde geraten, ein Attribut wie "Text" für den Kommentar hinzuzufügen, da Entitäten mindestens ein Attribut haben müssen.
- Die Entität Gärtner kann keine Attribute haben, da sie von der Entität Nutzer erbt. Das bedeutet, dass die Gärtner Entität alle Attribute von Nutzer übernimmt.
- In der BEWERTET Relation wurde das Attribut Skala hinzugefügt, da die Skala: 1-5 nur dann genutzt wird, wenn eine Bewertung für die Maßnahme existiert.
- Die Protokoll Entität hat zwei separate Attribute. Meiner Meinung nach sollte die Tabelle "Protokoll" später zwei entsprechende Spalten haben.  In dieser Tabelle kann der Gärtner Informationen darüber erstellen, welche Maßnahme mit welchem Erfolg durchgeführt wurde.





