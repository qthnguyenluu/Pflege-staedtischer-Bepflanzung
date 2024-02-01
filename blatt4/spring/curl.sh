#!/usr/bin/env sh

curl -X GET "http://localhost:8080/nutzer?email=jdoedoe%40ost.de"
curl -X POST "http://localhost:8080/nutzer" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "email=meowmeow@mail.com" -F "passwort=AB999ab" -F "vorname=Jayce" -F "nachname=Heinrich"
curl -X GET "http://localhost:8080/adressen?stadt=duesseldorf"
curl -X POST "http://localhost:8080/adressen" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "strasse=sunnystr." -F "hausnummer=10B" -F "plz=12345" -F "stadt=duesseldorf"
curl -X GET "http://localhost:8080/buerger?stadt=Duesseldorf"
curl -X POST "http://localhost:8080/buerger" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "email=tommaxx@lil.gg" -F "passwort=LL1234l" -F "vorname=Tom" -F "nachname=Maxxim" -F adresseid=4
curl -X GET "http://localhost:8080/gaertner?email=mann987%40exp.com"
curl -X POST "http://localhost:8080/gaertner" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "email=levi@joy.gg" -F "passwort=VI123l" -F "vorname=Levi" -F "nachname=Hoffnung" -F "spezialgebiet=Baumbau"
curl -X GET "http://localhost:8080/pflanzen?dname=apfel"
curl -X GET "http://localhost:8080/pflegemassnahmen?pflegeart=bewaesserung&min_avg_bewertung=2"
curl -X GET "http://localhost:8080/pflegeprotokolle"
curl -X GET "http://localhost:8080/pflegemassnahmen/bewertungen"
curl -X GET "http://localhost:8080/pflanzen/1/bilder"
curl -X DELETE "http://localhost:8080/adressen/5"
curl -X PATCH "http://localhost:8080/adressen/2" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "strasse=" -F "hausnummer=47" -F "plz=" -F "stadt="
curl -X POST "http://localhost:8080/pflanzen" -H "accept: */*" -u "jdoedoe@ost.de:1AB11a" -H "Content-Type: multipart/form-data" -F "lname=humulus" -F "dname=hopfen" -F "laengengrad=10.5843" -F "breitengrad=29.89" -F "pflanzentyp=Kletterpflanze" -F "datum=2023-12-06"
curl -X POST "http://localhost:8080/pflanzen/2/bilder" -H "accept: */*" -u "jdoedoe@ost.de:1AB11a" -H "Content-Type: multipart/form-data" -F "pfad=c:/src/Myphotos/favfoto_1.img"
curl -X DELETE -u "abc@email.com:000qqUZ" "http://localhost:8080/pflanzen/1/bilder/1"
curl -X PATCH "http://localhost:8080/pflanzen/1/bilder/2" -H "accept: */*" -u "abc@email.com:000qqUZ" -H "Content-Type: multipart/form-data" -F "pfad=c:/newphotos/my_fav_plant.img"
curl -X POST "http://localhost:8080/pflegemassnahmen" -H "accept: */*" -u "johnd@hotmail.com:POA777B" -H "Content-Type: multipart/form-data" -F "datum=2023-11-11 12:30:00" -F "pflegeart=bewaesserung"
curl -X GET "http://localhost:8080/pflegemassnahmen/buerger" -u "johnd@hotmail.com:POA777B"