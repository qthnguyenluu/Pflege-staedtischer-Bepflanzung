package de.hhu.cs.dbs.dbwk.project.presentation.rest;

import de.hhu.cs.dbs.dbwk.project.model.*;
import de.hhu.cs.dbs.dbwk.project.persistence.service.ApiService;
import de.hhu.cs.dbs.dbwk.project.security.CurrentUser;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequestMapping("/")
@PermitAll
@RestController
public class ExampleController {

    private final JdbcTemplate jdbcTemplate;

    private final ApiService apiService;
    public ExampleController(JdbcTemplate jdbcTemplate, ApiService apiService) {
        this.jdbcTemplate = jdbcTemplate;
        this.apiService = apiService;
    }

    @GetMapping
    // GET http://localhost:8080
    public String halloWelt() {
        return "\"Hallo Welt!\"";
    }

    @GetMapping("/exception")
    // GET http://localhost:8080
    public String halloException() {
        throw new RuntimeException("Hallo Exception!");
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers(){

            List<UserDTO> users = jdbcTemplate.query(
                    "SELECT rowid, * FROM Nutzer",
                    (rs, rowNum) -> new UserDTO(
                            rs.getInt("rowid"),
                            rs.getString("EMailAdresse"),
                            rs.getString("Password"),
                            rs.getString("Vorname"),
                            rs.getString("Nachname")
                    )
            ).stream().toList();
            if (users.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no users found");
            else return new ResponseEntity<List<UserDTO>>(users, HttpStatus.OK);
    }

    @GetMapping("/nutzer")
    public ResponseEntity<?> getUser(@RequestParam(value = "email", required = false) String email, @RequestParam(value = "nachname", required = false) String nachname){
        String query = "SELECT rowid, * FROM Nutzer WHERE EMailAdresse like ? OR Nachname like ? ";
        List<UserDTO> users = jdbcTemplate.query(query,
                (rs, rowNum) -> new UserDTO(
                        rs.getInt("rowid"),
                        rs.getString("EMailAdresse"),
                        rs.getString("Password"),
                        rs.getString("Vorname"),
                        rs.getString("Nachname")
                ), email, nachname).stream().toList();

        if (users.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no users found");
        else return new ResponseEntity<List<UserDTO>>(users, HttpStatus.OK);
//        else return ResponseEntity.status(HttpStatus.OK).body(users);

    }

    @PostMapping(value = "/nutzer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    public ResponseEntity<String> createUser(@RequestParam("email") String email,
                                             @RequestParam("passwort") String passwort,
                                             @RequestParam("vorname") String vorname,
                                             @RequestParam("nachname") String nachname) {
        //check email

        int row = 0;
        if (apiService.emailValidation(email)) {
            row = jdbcTemplate.update(
                    "INSERT INTO Nutzer(EMailAdresse, Password, Vorname, Nachname) VALUES (?, ?, ?, ?)",
                    email, passwort, vorname, nachname);
        }
        if (row != 0) {
            List<UserDTO> user = apiService.findUserDTObyEmail(email);
            return new ResponseEntity<>("new Nutzer added No. "+user.get(0).nutzerid(), apiService.addHeader("/nutzer",user.get(0).nutzerid()), HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new user can not be added");
    }

    @GetMapping("/adressen")
    public ResponseEntity<?> getAdressen(@RequestParam String stadt){
        String query = "SELECT * FROM Wohnort WHERE Stadt like ? ";
        List<WohnortDTO> adresse = jdbcTemplate.query(query,
                (rs, rowNum) -> new WohnortDTO(
                        rs.getInt("ID"),
                        rs.getString("Strasse"),
                        rs.getString("Hausnummer"),
                        rs.getString("PLZ"),
                        rs.getString("Stadt")
                ), stadt).stream().toList();

        if (adresse.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no results found");
        else return new ResponseEntity<List<WohnortDTO>>(adresse, HttpStatus.OK);

    }

    @PostMapping(value = "/adressen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createAdresse(@RequestParam("strasse") String strasse,
                                                @RequestParam("hausnummer") String hausnummer,
                                                @RequestParam("plz") String plz,
                                                @RequestParam("stadt") String stadt) {

        int rows = jdbcTemplate.update(
                "INSERT INTO Wohnort(Strasse, Hausnummer, PLZ, Stadt) VALUES (?, ?, ?, ?)",
                strasse, hausnummer, plz, stadt);

        if (rows != 0) {
                return new ResponseEntity<>("new Adresse added No. "+apiService.newAdresseID(), apiService.addHeader("/adressen",apiService.newAdresseID()), HttpStatus.CREATED);
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "can not be added");
    }

    @DeleteMapping(value = "/adressen/{adresseid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteAdresse(@PathVariable("adresseid") int id) {
        if (apiService.findBuergerHatWohnort(id).isEmpty()) {
            int row = jdbcTemplate.update("delete  from Wohnort where ID = ?", id);
            if (row != 0)
                return new ResponseEntity<>("Adresse deleted ", HttpStatus.NO_CONTENT);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no results found");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "can not be deleted");
    }

    //gibt Buerger zurueck
    @GetMapping("/buerger")
    public ResponseEntity<List<BuergerDTO>> getBuerger(@RequestParam String stadt){
        String query2 =" SELECT Buerger.*, Buerger.rowid as bid, Nutzer.rowid as nid, Nutzer.*, Wohnort.* " +
                "FROM Buerger " +
                "JOIN Nutzer ON Buerger.EMailAdresse = Nutzer.EMailAdresse " +
                "JOIN Wohnort ON Buerger.WohnortID = Wohnort.ID WHERE Wohnort.Stadt = ?";

        List<BuergerDTO> buergerDTOList = jdbcTemplate.query(query2,
                (rs, rowNum) -> new BuergerDTO(
                        rs.getInt("nid"),
                        rs.getInt("bid"),
                        rs.getInt("WohnortID"),
                        rs.getString("EMailAdresse"),
                        rs.getString("Password"),
                        rs.getString("Vorname"),
                        rs.getString("Nachname")
                ), stadt).stream().toList();

        if (buergerDTOList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no results found");
        else return ResponseEntity.status(HttpStatus.OK).body(buergerDTOList);
    }

    @PostMapping(value = "/buerger", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createBuerger(@RequestParam("email") String email,
                                                @RequestParam("passwort") String passwort,
                                                @RequestParam("vorname") String vorname,
                                                @RequestParam("nachname") String nachname,
                                                @RequestParam("adresseid") int adresseid) {

        String query = "SELECT rowid, * FROM Nutzer WHERE EMailAdresse like ? ";
        List<UserDTO> users = jdbcTemplate.query(query,
                (rs, rowNum) -> new UserDTO(
                        rs.getInt("rowid"),
                        rs.getString("EMailAdresse"),
                        rs.getString("Password"),
                        rs.getString("Vorname"),
                        rs.getString("Nachname")
                ), email).stream().toList();

        String query1 = "INSERT INTO Nutzer(EMailAdresse, Vorname, Nachname, Password) VALUES (?, ?, ?, ?)";
        String query2 = "insert into buerger(EmailAdresse, WohnortID) values (?, ?) ";

        if (!users.isEmpty() && apiService.emailValidation(email)){
            int row = jdbcTemplate.update(query2, email, adresseid);
            if (row != 0) {
                int newBuergerid = apiService.findBuergerDTObyEmail(email).get(0).buergerid();
                return new ResponseEntity<>("new Bürger added No. "+newBuergerid, apiService.addHeader("/buerger", newBuergerid), HttpStatus.CREATED);
            }
        } else if (users.isEmpty() && apiService.emailValidation(email)){
            int row1 = jdbcTemplate.update(query1, email, vorname, nachname, passwort);
            int row2 = jdbcTemplate.update(query2, email, adresseid);
            if (row1 != 0 && row2 != 0 && apiService.emailValidation(email)){
                int newBuergerid = apiService.findBuergerDTObyEmail(email).get(0).buergerid();
                return new ResponseEntity<>("new Bürger added No. "+ newBuergerid, apiService.addHeader("/buerger", newBuergerid), HttpStatus.CREATED);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new Bürger can not be added");
    }
    @PatchMapping (value = "/adressen/{adresseid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAdresse(@PathVariable("adresseid") int id,
                                               @RequestParam(value = "strasse", required = false) String strasse,
                                               @RequestParam(value = "hausnummer", required = false) String hausnummer,
                                               @RequestParam(value = "plz", required = false) String plz,
                                               @RequestParam(value = "stadt", required = false) String stadt) {

        List<Integer> updates = new ArrayList<>();

        if (!strasse.isBlank()) {
            int row = jdbcTemplate.update("update Wohnort set Strasse = ? where ID = ?", strasse, id);
            updates.add(row);
        }

        if (!hausnummer.isBlank()) {
            int row = jdbcTemplate.update("update Wohnort set Hausnummer = ? where ID = ?", hausnummer, id);
            updates.add(row);
        }

        if (!plz.isBlank()) {
            int row = jdbcTemplate.update("update Wohnort set PLZ = ? where ID = ?", plz, id);
            updates.add(row);
        }

        if (!stadt.isBlank()) {
            int row = jdbcTemplate.update("update Wohnort set Stadt = ? where ID = ?", stadt, id);
            updates.add(row);
        }
        boolean isUpdated = false;
        for (int ud: updates) {
             if(ud != 0) {
                 isUpdated = true;
             }
             break;
        }

        String query = "select * from Wohnort where ID = ? ";
        List<WohnortDTO> findAdById = jdbcTemplate.query(query,
                (rs, rowNum) -> new WohnortDTO(
                        rs.getInt("ID"),
                        rs.getString("Strasse"),
                        rs.getString("Hausnummer"),
                        rs.getString("PLZ"),
                        rs.getString("Stadt")
                ), id).stream().toList();
        if(findAdById.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no results found");
        else if(isUpdated)
            return new ResponseEntity<String>("Adresse updated", HttpStatus.NO_CONTENT);
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid informations");
    }
    @GetMapping("/gaertner")
    public ResponseEntity<List<GaertnerDTO>> getGaertner(@RequestParam String email){
        List<GaertnerDTO> gaertners = apiService.findGaertnerDTObyEmail(email);

        if (gaertners.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no results found");
        else return ResponseEntity.status(HttpStatus.OK).body(gaertners);
    }

    //POST gaertner
    @PostMapping(value = "/gaertner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createGaertner(@RequestParam("email") String email,
                                                @RequestParam("passwort") String passwort,
                                                @RequestParam("vorname") String vorname,
                                                @RequestParam("nachname") String nachname,
                                                @RequestParam("spezialgebiet") String spezialgebiet) {

        List<UserDTO> users = apiService.findUserDTObyEmail(email);

        String findByName = "SELECT rowid, * FROM Spezialisierung WHERE Bezeichnung like ? ";
        List<Spezialgebiet> gebiet = jdbcTemplate.query(findByName,
                (rs, rowNum) -> new Spezialgebiet(
                        rs.getInt("ID"),
                        rs.getString("Bezeichnung")
                ), spezialgebiet).stream().toList();

        String query1 = "INSERT INTO Nutzer(EMailAdresse, Vorname, Nachname, Password) VALUES (?, ?, ?, ?)";
        String query2 = "insert into Gaertner(EmailAdresse) values (?)";
        //find spezialisierung by bezeichnung
        String query3 = "insert into Spezialisierung(Bezeichnung) values (?)";
        String query4 = "insert into GaertnerHatSpezialisierung(NutzerEmailAdresse, SpezialisierungID) values (?,?)";
        //user found
        int addHatSpezialisierung = 0, addSpezialisierung = 0;
        if (!users.isEmpty() && apiService.emailValidation(email)) {
            //gebiet found
            if (!gebiet.isEmpty()) {
                //add spezialisierung
                addSpezialisierung = 1;
                jdbcTemplate.update(query2, email);
                addHatSpezialisierung = jdbcTemplate.update(query4, email, gebiet.get(0).spid());
            } else {
                addSpezialisierung = jdbcTemplate.update(query3, spezialgebiet);
                if (addSpezialisierung != 0) {
                    jdbcTemplate.update(query2, email);
                    addHatSpezialisierung = jdbcTemplate.update(query4, email, apiService.newGebiettid());
                }
            }
            //add gaertner
            if (addSpezialisierung != 0 && addHatSpezialisierung != 0) {
                int newGaertnerid = apiService.findGaertnerDTObyEmail(email).get(0).gaertnerid();
                return new ResponseEntity<>("new Gaertner added No. "+newGaertnerid, apiService.addHeader("/gaertner", newGaertnerid), HttpStatus.CREATED);
            }
        } else if (users.isEmpty() && apiService.emailValidation(email)) {
            //add user
            int addNutzer = jdbcTemplate.update(query1, email, vorname, nachname, passwort);
            if (!gebiet.isEmpty()) {
                //spezialisierung found
                addSpezialisierung = 1;
                jdbcTemplate.update(query2, email);
                addHatSpezialisierung = jdbcTemplate.update(query4, email, gebiet.get(0).spid());
            } else {
                addSpezialisierung = jdbcTemplate.update(query3, spezialgebiet);
                if (addSpezialisierung != 0) {
                    jdbcTemplate.update(query2, email);
                    addHatSpezialisierung = jdbcTemplate.update(query4, email, apiService.newGebiettid());
                }
            }
            if (addNutzer != 0 && addSpezialisierung != 0 && addHatSpezialisierung != 0 && apiService.emailValidation(email)) {
                int newGaertnerid = apiService.findGaertnerDTObyEmail(email).get(0).gaertnerid();
                return new ResponseEntity<>("new Gaertner added No. "+newGaertnerid, apiService.addHeader("/gaertner", newGaertnerid), HttpStatus.CREATED);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new Gaertner can not be added");
    }
    //GET Pflanzen
    @GetMapping("/pflanzen")
    public ResponseEntity<?> getPflanzen(@RequestParam String dname){
        String query = "SELECT Pflanze.ID as pid, Pflanzentyp.Bezeichnung as be, * FROM Pflanze " +
                "JOIN Standort ON Pflanze.StandortID = Standort.ID " +
                "JOIN Pflanzentyp ON Pflanzentyp.ID = Pflanze.PflanzentypID " +
                "JOIN PflanzeGepflanzt_vonGaertner ON Pflanze.ID = PflanzeGepflanzt_vonGaertner.PflanzeID " +
                "WHERE  Pflanze.deutscheBezeichnung like ? ";
        List<PflanzeDTO> pflanzen = jdbcTemplate.query(query,
                (rs, rowNum) -> new PflanzeDTO(
                        rs.getInt("pid"),
                        rs.getString("lateinischeBezeichnung"),
                        rs.getString("deutscheBezeichnung"),
                        rs.getDouble("Laengengrad"),
                        rs.getDouble("Breitengrad"),
                        rs.getString("be"),
                        rs.getString("Pflanzdatum")
                ), dname).stream().toList();

        if (pflanzen.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no pflanzen found");
        else return ResponseEntity.status(HttpStatus.OK).body(pflanzen);
    }

    @GetMapping("/pflegemassnahmen")
    public ResponseEntity<?> getPflegemassnahme(@RequestParam(value = "pflegeart", required = false) String pflegeart, @RequestParam(value = "min_avg_bewertung", required = false) double min_avg_bewertung){
        String query = "SELECT AVG(GaertnerBewertetPflegemassnahme.Skala) AS a, Pflegemassnahme.ID as pfid, * "+
                "FROM Pflegemassnahme " +
                "JOIN GaertnerBewertetPflegemassnahme ON Pflegemassnahme.ID = GaertnerBewertetPflegemassnahme.PflegemassnahmeID "+
                "JOIN Pflegeart ON Pflegeart.ID = Pflegemassnahme.PflegeartID " +
                "Where Pflegeart.Name like ? " +
                "GROUP BY GaertnerBewertetPflegemassnahme.PflegemassnahmeID "+
                "HAVING a >= ?";

        List<PflegemassnahmeDTO> pflegemassnahme = jdbcTemplate.query(query,
                (rs, rowNum) -> new PflegemassnahmeDTO(
                        rs.getInt("pfid"),
                        rs.getDouble("a"),
                        rs.getString("Datum"),
                        rs.getString("Name")
                ),pflegeart, min_avg_bewertung).stream().toList();

        if (pflegemassnahme.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no results found");
        else return ResponseEntity.status(HttpStatus.OK).body(pflegemassnahme);
    }
    @GetMapping("/pflegeprotokolle")
    public ResponseEntity<?> getProtokolle(){
        String query = "select COUNT(pflegemassnahmeid) as c, Gaertner.rowid as gid, * " +
                "From PflegeprotokollEnthaeltPflegemassnahme pp " +
                "join Pflegeprotokoll On Pflegeprotokoll.ID = pp.PflegeprotokollID " +
                "JOIN Gaertner On Pflegeprotokoll.NutzerEMailAdresse = Gaertner.EMailAdresse " +
                "GROUP by pp.pflegeprotokollID  ";

        List<PflegeprotokollDTO> protokolle = jdbcTemplate.query(query,
                (rs, rowNum) -> new PflegeprotokollDTO(
                        rs.getInt("c"),
                        rs.getInt("gid")
                )).stream().toList();

        if (protokolle.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no results found");
        else return ResponseEntity.status(HttpStatus.OK).body(protokolle);
    }

    @GetMapping("/pflegemassnahmen/bewertungen")
    public ResponseEntity<?> getBewertung(){
        String query = "select GaertnerBewertetPflegemassnahme.rowid as bid, Gaertner.rowid as gid, * from GaertnerBewertetPflegemassnahme " +
        "Join Gaertner On GaertnerBewertetPflegemassnahme.NutzerEMailAdresse = Gaertner.EMailAdresse";

        List<BewertungDTO> bewertung = jdbcTemplate.query(query,
                (rs, rowNum) -> new BewertungDTO(
                        rs.getInt("PflegemassnahmeID"),
                        rs.getInt("bid"),
                        rs.getInt("gid")
                )).stream().toList();

        if (bewertung.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no results found");
        else return ResponseEntity.status(HttpStatus.OK).body(bewertung);
    }

    //get bilder
    @GetMapping("/pflanzen/{pflanzeid}/bilder")
    public ResponseEntity<?> getPflanzenBild(@PathVariable("pflanzeid") int pflanzeid){
        String query = "SELECT Pflanze.ID as pid, * FROM Pflanze " +
                "JOIN PflanzeGespeichert_unterBild ON PflanzeGespeichert_unterBild.PflanzeID = Pflanze.ID " +
                "JOIN Bild ON PflanzeGespeichert_unterBild.BildID = Bild.ID " +
                "WHERE  Pflanze.ID = ? ";

        List<PflanzeBilderDTO> bilder = jdbcTemplate.query(query,
                (rs, rowNum) -> new PflanzeBilderDTO(
                        rs.getInt("pid"),
                        rs.getString("Bildpfad")
                ), pflanzeid).stream().toList();

        if (bilder.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no bilder found");
        else return ResponseEntity.status(HttpStatus.OK).body(bilder);
    }

    //sua pflanzentyp

    @PostMapping(value = "/pflanzen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RolesAllowed("BUERGER")
    public ResponseEntity<String> createPflanzen(@CurrentUser User user,
                                                 @RequestParam("dname") String dname,
                                                 @RequestParam("lname") String lname,
                                                 @RequestParam("laengengrad") double laengengrad,
                                                 @RequestParam("breitengrad") double breitengrad,
                                                 @RequestParam("pflanzentyp") String pflanzentyp,
                                                 @RequestParam(value = "datum", required = false) String datum) {

        String query1 = "insert into Pflanze(deutscheBezeichnung, lateinischeBezeichnung, NutzerEmailAdresse, StandortID, PflanzentypID, Datum) values (?,?,?,?,?,?)";
        String addWithoutDatum = "insert into Pflanze(deutscheBezeichnung, lateinischeBezeichnung, NutzerEmailAdresse, StandortID, PflanzentypID) values (?,?,?,?,?)";
        String query2 = "insert into Standort(Breitengrad, Laengengrad) values (?,?)";
        String query3 = "insert into Pflanzentyp(Bezeichnung) values (?)";

        int addStandort, addTyp, addPflanze = 0;
        addStandort = jdbcTemplate.update(query2, breitengrad, laengengrad);
        //pflanzentyp found
        if (!apiService.findPflanzentyp(pflanzentyp).isEmpty() && addStandort != 0) {
            if (!datum.isBlank())
                addPflanze = jdbcTemplate.update(query1, dname, lname, user.getUniqueString(), apiService.newStandortid(), apiService.findPflanzentyp(pflanzentyp).get(0).tid(), datum);
            if (datum.isBlank())
                addPflanze = jdbcTemplate.update(addWithoutDatum, dname, lname, user.getUniqueString(), apiService.newStandortid(), apiService.findPflanzentyp(pflanzentyp).get(0).tid());
//            if (addPflanze != 0) {
        } else if (apiService.findPflanzentyp(pflanzentyp).isEmpty() && addStandort != 0) {
            addTyp = jdbcTemplate.update(query3, pflanzentyp);
            if (addTyp != 0) {
                if (!datum.isBlank())
                    addPflanze = jdbcTemplate.update(query1, dname, lname, user.getUniqueString(), apiService.newStandortid(), apiService.newPflanzentypID(), datum);
                if (datum.isBlank())
                    addPflanze = jdbcTemplate.update(addWithoutDatum, dname, lname, user.getUniqueString(), apiService.newStandortid(), apiService.newPflanzentypID());
            }
        }
        if (addPflanze != 0) {
            int newPflanzeid = apiService.newPflanzeID();
            return new ResponseEntity<>("new Pflanze added No. "+newPflanzeid, apiService.addHeader("/pflanzen", newPflanzeid), HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new Pflanze can not be added");
    }

    @PostMapping(value = "/pflanzen/{pflanzeid}/bilder", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RolesAllowed("BUERGER")
    public ResponseEntity<String> addBilder(@CurrentUser User user, @PathVariable("pflanzeid") int pflanzeid, @RequestParam("pfad") String pfad) {
        String query1 = "insert into PflanzeGespeichert_unterBild(PflanzeID, BildID) values (?,?)";
        String query2 = "insert into Bild(Bildpfad) values (?)";
        String findBild = "select * from Bild where Bildpfad like ?";
        List<Bild> bild = jdbcTemplate.query(findBild,
                 (rs, rowNum) -> new Bild(
                 rs.getInt("ID"),
                 rs.getString("Bildpfad")), pfad).stream().toList();
//        bild found
        int addNewPfad, addNewBild;
        if (!bild.isEmpty()) {
            addNewPfad = jdbcTemplate.update(query1, pflanzeid, bild.get(0).id());
            if (addNewPfad != 0) {
                return new ResponseEntity<>("new Bild added No. "+bild.get(0).id(), apiService.addHeader("/pflanzen/bilder", bild.get(0).id()), HttpStatus.CREATED);
            }
        }
        else {
            addNewBild = jdbcTemplate.update(query2, pfad);
            if (addNewBild != 0) {
                addNewPfad = jdbcTemplate.update(query1, pflanzeid, apiService.newBildID());
                if (addNewPfad != 0) {
                    int newBildid = apiService.newBildID();
                    return new ResponseEntity<>("new Bild to Pflanze added No. "+newBildid, apiService.addHeader("/pflanzen/bilder", newBildid), HttpStatus.CREATED);
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new Bild can not be added");
    }

    @GetMapping("/pflegemassnahmen/buerger")
    @RolesAllowed("GAERTNER")
    // GET http://localhost:8080/foo3 => FORBIDDEN. Siehe SQLiteUserRepository.
    public ResponseEntity<?> getBuergerOfMassnahme(@CurrentUser User user) {
        String query = "select Buerger.rowid as buergerid,PflegemassnahmeID, * from BuergerNimmtteilPflegemassnahme " +
                "join Buerger On Buerger.EMailAdresse = BuergerNimmtteilPflegemassnahme.nutzeremailadresse";
        List<BuergeridDTO> IDs = jdbcTemplate.query(query,
                (rs, rowNum) -> new BuergeridDTO(
                        rs.getInt("buergerid"),
                        rs.getInt("PflegemassnahmeID")
                )).stream().toList();

        if (IDs.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no results found");
        else return ResponseEntity.status(HttpStatus.OK).body(IDs);
    }

    @DeleteMapping(value = "/pflanzen/{pflanzeid}/bilder/{bildid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> deleteAdresse(@CurrentUser User user, @PathVariable("pflanzeid") int pflanzeid,
                                           @PathVariable("bildid") int bildid) {
        int row = jdbcTemplate.update("delete from PflanzeGespeichert_unterBild where PflanzeID = ? and BildID = ?", pflanzeid, bildid);
        if (row != 0) {
            return new ResponseEntity<>("bild "+ bildid + "of pflanze "+ pflanzeid + " deleted", HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no results found");
        }
    }


    @PatchMapping (value = "/pflanzen/{pflanzeid}/bilder/{bildid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RolesAllowed("BUERGER")
    public ResponseEntity<String> updateAdresse(@CurrentUser User user,
                                                @PathVariable("pflanzeid") int pflanzeid,
                                                @PathVariable("bildid") int bildid,
                                                @RequestParam("pfad") String pfad) {

        String querydelete = "delete from PflanzeGespeichert_unterBild where PflanzeID = ? and BildID = ? ";
        if (apiService.findBildVonPflanze(pflanzeid, bildid).isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // old Bild found
        if (!apiService.findBild(pfad).isEmpty()) {
            //find old bildId
            int bildIdFound = apiService.findBild(pfad).get(0).id();
            jdbcTemplate.update(querydelete, pflanzeid, bildid);
            jdbcTemplate.update("insert into PflanzeGespeichert_unterBild(PflanzeID, BildID) values (?,?)", pflanzeid, bildIdFound);
            return new ResponseEntity<String>("New Bildpfad updated", HttpStatus.NO_CONTENT);
        } else {
            int addNewBild = jdbcTemplate.update("insert into Bild(Bildpfad) values (?)", pfad);
            if (addNewBild != 0) {
                jdbcTemplate.update(querydelete, pflanzeid, bildid);
                jdbcTemplate.update("insert into PflanzeGespeichert_unterBild(PflanzeID, BildID) values (?,?)", pflanzeid, apiService.newBildID());
                return new ResponseEntity<String>("New Bildpfad updated", HttpStatus.NO_CONTENT);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid new pfad");
    }

    @PostMapping(value = "/pflegemassnahmen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RolesAllowed("GAERTNER")
    public ResponseEntity<String> addPflegemassnahme(@CurrentUser User user, @RequestParam("datum") String datum, @RequestParam("pflegeart") String pflegeart) {
        String query = "insert into Pflegeart(Name) values (?)";
        String query2 = "insert into Pflegemassnahme(Datum, PflegeartID, NutzerEMailAdresse) values (?,?,?)";

        if (!apiService.findPflegeart(pflegeart).isEmpty()) {
                //find old pflegeartId
            int row = jdbcTemplate.update(query2, datum, apiService.findPflegeart(pflegeart).get(0).paid(), user.getUniqueString());
            if (row != 0)
                return new ResponseEntity<>("new pflegemassnahme added No. "+apiService.newPflegemassnahmeID(), apiService.addHeader("/pflegemassnahmen", apiService.newPflegemassnahmeID()), HttpStatus.CREATED);
        }
        if (apiService.findPflegeart(pflegeart).isEmpty()){
            int addNewPflegeart = jdbcTemplate.update(query, pflegeart);
            if (addNewPflegeart != 0) {
                int row = jdbcTemplate.update(query2, datum, apiService.findPflegeart(pflegeart).get(0).paid(), user.getUniqueString());
                if (row != 0)
                    return new ResponseEntity<>("new pflegemassnahme added No. "+apiService.newPflegemassnahmeID(), apiService.addHeader("/pflegemassnahmen", apiService.newPflegemassnahmeID()), HttpStatus.CREATED);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid input info");
    }

    @GetMapping("foo")
    @RolesAllowed("NUTZER")
    // GET http://localhost:8080/foo => OK. Siehe SQLiteUserRepository.
    public String halloFoo(@CurrentUser User user) {
        return "\"Hallo " + user.getUniqueString() + "\"!";
    }

    @GetMapping("foo2")
    @RolesAllowed("BUERGER")
    // GET http://localhost:8080/foo2 => FORBIDDEN. Siehe SQLiteUserRepository.
    public String halloFoo2(@CurrentUser User user) {
        return "\"Hallo " + user.getUniqueString() + "\"!";
    }

    @GetMapping("foo3")
    @RolesAllowed("GAERTNER")
    // GET http://localhost:8080/foo3 => FORBIDDEN. Siehe SQLiteUserRepository.
    public String halloFoo3(@CurrentUser User user) {
        return "\"Hallo " + user.getUniqueString() + "\"!";
    }

    @GetMapping("foo3/{foo}")
    // GET http://localhost:8080/foo3/bar
    public String halloFoo3(@PathVariable("foo") String foo) {
        if (!foo.equals("bar")) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return "\"Hallo " + foo + "\"!";
    }

    @GetMapping("foo4")
    // GET http://localhost:8080/foo4?bar=xyz
    public String halloFoo4(@RequestParam("bar") String bar) {
        return "\"Hallo " + bar + "\"!";
    }

    @GetMapping("bar")
    // GET http://localhost:8080/bar => BAD REQUEST; http://localhost/bar?foo=xyz => OK
    public ResponseEntity<?> halloBar(@RequestParam(name = "foo", required = false) String foo) {
        if (foo == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "foo must not be foo.");
        return ResponseEntity.status(HttpStatus.OK).body("Hallo " + foo + "!");
    }

    @GetMapping("bar2")
    // GET http://localhost:8080/bar2
    public String halloBar2(
            @RequestParam(name = "name", defaultValue = "Max Mustermann") List<String> names) {
        int random = ThreadLocalRandom.current().nextInt(0, names.size());
        return jdbcTemplate.queryForObject("SELECT ?", String.class, names.get(random));
    }

    @PostMapping("foo")
    // POST http://localhost:8080/foo
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file, UriComponentsBuilder uriComponentsBuilder) {
        try {
            String length = String.valueOf(file.getBytes().length);
            return ResponseEntity.created(uriComponentsBuilder.path("/{id}").build(length)).build();
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, null, exception);
        }
    }
}
