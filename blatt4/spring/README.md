# Blatt 4

Umsetzung eines dazugehörigen RESTful Web Services

> ⚠️ Um dieses API-Template zu nutzen, muss `de.hhu.cs.dbs.dbwk.project.api=spring` in [`gradle.properties`](../../gradle.properties) gesetzt werden.

> ⚠️ Unter Umständen müssen die vorhandenen Dateien dieses API-Templates angepasst werden.

## Anleitung

### Allgemein

Die Mainklasse ist [`Application`](src/main/java/de/hhu/cs/dbs/dbwk/project/Application.java).
Nachdem das System mit bspw.

```shell
./gradlew :blatt4:spring:runContainerized
```

gestartet wurde, kann mit [cURL](#nützliche-links) oder [Swagger UI](#nützliche-links) (per http://localhost:8090) die
API getestet werden.

Das System kann mit

```shell
./gradlew :blatt4:spring:composeDown
```

gestoppt werden.

Alle Endpunkte der bereitgestellten OpenAPI-Spezifikation müssen im
Package [`de.hhu.cs.dbs.dbwk.project.presentation.rest`](src/main/java/de/hhu/cs/dbs/dbwk/project/presentation/rest) hinzugefügt
werden. Die darin enthaltene Klasse [**`ExampleController`**](src/main/java/de/hhu/cs/dbs/dbwk/project/presentation/rest/ExampleController.java) dient als Beispiel dafür.

Ob in den Controllern selbst mit der Datenbank kommuniziert wird oder dafür im
Package [`de.hhu.cs.dbs.dbwk.project.persistence.sql.sqlite`](src/main/java/de/hhu/cs/dbs/dbwk/project/persistence/sql/sqlite)
entsprechende Repositories angelegt werden, kann ausgesucht werden. Vergessen Sie nicht eine korrekte Authentifizierung
und Autorisierung zu implementieren, indem Sie die
Interfaces [`UserRepository`](src/main/java/de/hhu/cs/dbs/dbwk/project/model/UserRepository.java)
und [`RoleRepository`](src/main/java/de/hhu/cs/dbs/dbwk/project/model/RoleRepository.java) des
Package [`de.hhu.cs.dbs.dbwk.project.model`](src/main/java/de/hhu/cs/dbs/dbwk/project/model) im
Package [`de.hhu.cs.dbs.dbwk.project.persistence.sql.sqlite`](src/main/java/de/hhu/cs/dbs/dbwk/project/persistence/sql/sqlite) als
Spring Bean umsetzen.

> 🚨 Die Anforderungen der Aufgabenstellung müssen eingehalten werden.

### Wichtig

Sollte sich etwas an der API-Spezifikation ändern, so können Sie mit

```shell
./gradlew :blatt4:spring:updateSpecification
```

Ihre lokale Kopie aktualisieren. Diese wird zudem alle 30 Sekunden invalidiert, sodass sie automatisch beim nächsten
Start der Anwendung per Gradle neu geladen wird.

Wenn es Probleme beim Invalidieren gibt, so können Sie mit

```shell
./gradlew :blatt4:spring:clean --refresh-dependencies
```

die mit Gradle generierten Dateien löschen und die Abhängigkeiten manuell neu laden.

### Nützliche Links

- [Adoptium](https://adoptium.net/de/)
- [cURL](https://curl.haxx.se)
- [Docker](https://www.docker.com)
- [Gradle](https://gradle.org)
- [Spring](https://spring.io)
- [Swagger](https://swagger.io)

## Kritische Entscheidungen

- Die Example Controlle wurde weiterhin genutzt und für die Endpunkte erweitert.
- SQLite unterstützt die Funktion REGEXP nicht so richtig. Um dies in Java zum Laufen zu bringen, müssen einige externe Packages in die API eingebunden werden. Das funktioniert bei mir jedoch nicht. Ich habe mit Hilfe von REGEXP eine Funktion 'EmailValidation' in Java umgeschrieben und in der API-Service reingepackt, der die gleiche Funktion wie REGEXP in Phase 3 hat.

- Beim Endpunkt GET/Nutzer funktioniert die API so, dass nach einem Nutzer entweder anhand der E-Mail-Adresse oder des Nachnamens gesucht werden kann. Die Parameter sind nicht erforderlich. 
- Ein Nutzer kann sowohl Bürger als auch Gärtner sein. Bei den Endpunkten POST /buerger und /gaertner, wenn eine E-Mail-Adresse bereits in der Nutzer-Tabelle existiert, kann dieselbe E-Mail-Adresse sowohl als Bürger als auch als Gärtner hinzugefügt werden. Alle anderen Infos wie Namen und Passwort werden aus der Tabelle Nutzer übernommen.

- Der HTTP code 204 (HTTP No Content) muss keinen Nachrichten-Body enthalten. Bei dem Endpunkt PATCH /adresse führt die API bei gleichzeitig leeren Werten für alle vier Parameter zu einem 400 Bad Request Code.

- Bei POST /pflegemassnahmen (Nr. 15) muss der Parameter 'datum' im Format yyyy-mm-dd hh:mm:ss sein. 
- Eine Spalte für das Datum (optionales Attribut) wurde nachträglich zur Tabelle Pflanze hinzugefügt. Es gibt für dieses Attribut keine Validierung. Es könnte jedoch auch mit REGEXP im Java-Code geschrieben werden, aber dafür fehlt mir momentan die Zeit. Deswegen sollte es im korrekten Format 'yyyy-mm-dd' oder "send empty value" eingegeben werden.
