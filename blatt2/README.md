# Blatt 2

Überführung des ER-Modells in ein Relationenmodell

## Kritische Entscheidungen

- Ich habe zwei Fehler gemäß der Korrektur behoben. Das Attribut Protokoll.Information wurde gelöscht, und das Pflanzdatum wurde an die Beziehung "gepflanzt_von" angehängt.
- Die Beziehungen wurden als E1RE2 bezeichnet, zum Beispiel BuergerHatWohnort, um sie klar voneinander zu unterscheiden.
- Die ID wurde als künstlicher Schlüssel für 
Entity hinzugefügt, für die kein primärer Schlüssel vorhanden ist.
- Bürger und Gärtner haben die E-Mail-Adresse als Primär- bzw. Fremdschlüssel von Nutzer geerbt. Durch diesen Schlüssel ist es möglich, auf Information des Nutzers zuzugreifen.
- Die Verschmelzung wurde durchgeführt, wo die Kardinalität [1,1] vorkommt.
- Verschmelzungen: Buerger + BuergerHatWohnort, Pflanze + BuergerTraegt_einPflanze, Pflanze + PflanzeWaechst_anStandort, Pflanze + PflanzeGehoert_zuPflanzentyp, Pflegemassnahme + PflegemassnahmeZugeordnetPflegeart, Pflegeprotokoll + GaertnerErstelltPflegeprotokoll, Pflegemassnahme + GaertnerNimmt_teilPflegemassnahme
