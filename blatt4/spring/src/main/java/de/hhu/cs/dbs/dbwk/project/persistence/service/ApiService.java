package de.hhu.cs.dbs.dbwk.project.persistence.service;

import de.hhu.cs.dbs.dbwk.project.model.*;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ApiService {
    private final JdbcTemplate jdbcTemplate;

    public ApiService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean emailValidation(String email) {
        String emailRegex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public List<UserDTO> findUserDTObyEmail(String email) {
        List<UserDTO> users = jdbcTemplate.query("select rowid, * from Nutzer where EMailAdresse like ?",
                (rs, rowNum) -> new UserDTO(
                        rs.getInt("rowid"),
                        rs.getString("EMailAdresse"),
                        rs.getString("Password"),
                        rs.getString("Vorname"),
                        rs.getString("Nachname")
                ), email).stream().toList();
        return users;
    }

    public HttpHeaders addHeader (String newpath, int id) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentServletMapping().path(newpath+"/{id}").build()
                .expand(id).toUri();

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setLocation(location);
        return headers;
    }
    public List<BuergerDTO> findBuergerDTObyEmail(String email) {
        String query =" SELECT Buerger.*, Buerger.rowid as bid, Nutzer.rowid as nid, Nutzer.*, Wohnort.* " +
                "FROM Buerger " +
                "JOIN Nutzer ON Buerger.EMailAdresse = Nutzer.EMailAdresse " +
                "JOIN Wohnort ON Buerger.WohnortID = Wohnort.ID WHERE Buerger.EMailAdresse like ?";

        List<BuergerDTO> buergerDTOList = jdbcTemplate.query(query,
                (rs, rowNum) -> new BuergerDTO(
                        rs.getInt("nid"),
                        rs.getInt("bid"),
                        rs.getInt("WohnortID"),
                        rs.getString("EMailAdresse"),
                        rs.getString("Password"),
                        rs.getString("Vorname"),
                        rs.getString("Nachname")
                ), email).stream().toList();
        return buergerDTOList;
    }

    public List<GaertnerDTO> findGaertnerDTObyEmail(String email) {
        String query = "SELECT Nutzer.rowid as nid, Gaertner.rowid as gid, * FROM Nutzer " +
                "JOIN Gaertner ON Nutzer.EMailAdresse = Gaertner.EMailAdresse " +
                "JOIN GaertnerHatSpezialisierung ON GaertnerHatSpezialisierung.NutzerEMailAdresse = Gaertner.EMailAdresse " +
                "JOIN Spezialisierung ON Spezialisierung.ID = GaertnerHatSpezialisierung.SpezialisierungID " +
                "WHERE  Gaertner.EMailAdresse like ? ";
        List<GaertnerDTO> gaertners = jdbcTemplate.query(query,
                (rs, rowNum) -> new GaertnerDTO(
                        rs.getInt("nid"),
                        rs.getInt("gid"),
                        rs.getString("EMailAdresse"),
                        rs.getString("Password"),
                        rs.getString("Vorname"),
                        rs.getString("Nachname"),
                        rs.getString("Bezeichnung")
                ), email).stream().toList();
        return gaertners;
    }
    public int newAdresseID() {
        String query = "select max(ID) from Wohnort";
        return jdbcTemplate.queryForObject(query,Integer.class);
    }

    public List<BuergerHatWohnort> findBuergerHatWohnort(int wohnortid) {
        String query = "select * from Buerger where WohnortID = ?";
        return jdbcTemplate.query(query,
                (rs, rowNum) -> new BuergerHatWohnort(
                        rs.getString("EMailAdresse"),
                        rs.getInt("WohnortID")
                ), wohnortid).stream().toList();

    }

    public List<Pflanzentyp> findPflanzentyp(String bezeichnung) {
        String query = "select * from Pflanzentyp where Bezeichnung like ?";
        List<Pflanzentyp> typ = jdbcTemplate.query(query,
                (rs, rowNum) -> new Pflanzentyp(
                        rs.getInt("ID"),
                        rs.getString("Bezeichnung")
                ), bezeichnung).stream().toList();
        return typ;
    }

//    public Standort findStandort(double breitengrad, double laengengrad) {
//        String query = "select * from Standort where Laengengrad = ? and Breitengrad = ?";
//        return jdbcTemplate.queryForObject(query,
//                (rs, rowNum) -> new Standort(
//                        rs.getInt("ID"),
//                        rs.getDouble("Breitengrad"),
//                        rs.getDouble("Laengengrad")
//                ), breitengrad, laengengrad);
//    }
    public int newStandortid() {
        String query = "select max(ID) from Standort";
        return jdbcTemplate.queryForObject(query,Integer.class);
    }
    public int newGebiettid() {
        String query = "select max(ID) from Spezialisierung";
        return jdbcTemplate.queryForObject(query,Integer.class);
    }
    public List<Bild> findBild(String pfad) {
        String query2 = "select * from Bild where Bildpfad like ? ";
        return jdbcTemplate.query(query2, ((rs, rowNum) -> new Bild(rs.getInt("ID"), rs.getString("Bildpfad"))), pfad).stream().toList();
    }
    public int newPflanzeID() {
        String query = "select max(ID) from Pflanze";
        return jdbcTemplate.queryForObject(query,Integer.class);
    }

    public List<BildVonPflanze> findBildVonPflanze(int pflanzeid, int bildid) {
        String query = "select * from PflanzeGespeichert_unterBild where PflanzeID = ? and BildID = ?";
        return jdbcTemplate.query(query, (rs, rowNum) -> new BildVonPflanze(rs.getInt("PflanzeID"), rs.getInt("BildID")), pflanzeid, bildid);
    }

    public List<Pflegeart> findPflegeart(String pflegeart) {
        String query = "select * from Pflegeart where Name like ? ";
        return jdbcTemplate.query(query, ((rs, rowNum) -> new Pflegeart(rs.getInt("ID"), rs.getString("Name"))), pflegeart).stream().toList();
    }
    public int newPflanzentypID() {
        String query = "select max(ID) from Pflanzentyp";
        return jdbcTemplate.queryForObject(query,Integer.class);
    }

    public int newBildID() {
        String query = "select max(ID) from Bild";
        return jdbcTemplate.queryForObject(query,Integer.class);
    }

    public int newPflegemassnahmeID() {
        String query = "select max(ID) from Pflegemassnahme";
        return jdbcTemplate.queryForObject(query,Integer.class);
    }
}
