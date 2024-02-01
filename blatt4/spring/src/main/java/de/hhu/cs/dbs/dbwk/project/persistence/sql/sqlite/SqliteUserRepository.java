package de.hhu.cs.dbs.dbwk.project.persistence.sql.sqlite;

import de.hhu.cs.dbs.dbwk.project.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("userRepository")
public class SqliteUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public SqliteUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findUser(String uniqueString) {

        String query4 ="SELECT Gaertner.EMailAdresse, Nutzer.Password " +
                "FROM Gaertner " +
                "JOIN Nutzer ON Gaertner.EMailAdresse = Nutzer.EMailAdresse " +
                "JOIN Buerger ON Buerger.EMailAdresse = Gaertner.EMailAdresse " +
                "WHERE Gaertner.EMailAdresse like ?";

        List<NutzerDAO> findThreeRoles = jdbcTemplate.query(query4,
                (rs, rowNum) -> new NutzerDAO(
                        rs.getString("EMailAdresse"),
                        rs.getString("Password")
                ), uniqueString).stream().toList();

        if(!findThreeRoles.isEmpty())
            return Optional.of(new SimpleUser(uniqueString, "{noop}"+findThreeRoles.get(0).passwort(), Set.of(new SimpleRole("NUTZER"), new SimpleRole("BUERGER"), new SimpleRole("GAERTNER"))));



        String query2 ="SELECT Buerger.EMailAdresse, Nutzer.Password " +
                "FROM Buerger " +
                "JOIN Nutzer ON Buerger.EMailAdresse = Nutzer.EMailAdresse " +
                "WHERE Buerger.EMailAdresse like ?";

        List<NutzerDAO> findBuerger = jdbcTemplate.query(query2,
                (rs, rowNum) -> new NutzerDAO(
                        rs.getString("EMailAdresse"),
                        rs.getString("Password")
                ), uniqueString).stream().toList();

        if(!findBuerger.isEmpty())
            return Optional.of(new SimpleUser(uniqueString, "{noop}"+findBuerger.get(0).passwort(), Set.of(new SimpleRole("NUTZER"), new SimpleRole("BUERGER"))));

        String query3 ="SELECT Gaertner.EMailAdresse, Nutzer.Password " +
                "FROM Gaertner " +
                "JOIN Nutzer ON Gaertner.EMailAdresse = Nutzer.EMailAdresse " +
                "WHERE Gaertner.EMailAdresse like ?";
        List<NutzerDAO> findGaertner = jdbcTemplate.query(query3,
                (rs, rowNum) -> new NutzerDAO(
                        rs.getString("EMailAdresse"),
                        rs.getString("Password")
                ), uniqueString).stream().toList();

        if(!findGaertner.isEmpty())
            return Optional.of(new SimpleUser(uniqueString, "{noop}"+findGaertner.get(0).passwort(), Set.of(new SimpleRole("NUTZER"), new SimpleRole("GAERTNER"))));


        List<NutzerDAO> findNutzer = jdbcTemplate.query("select Nutzer.EMailAdresse, Nutzer.Password from Nutzer where EMailAdresse like ?",
                (rs, rowNum) -> new NutzerDAO(
                        rs.getString("EMailAdresse"),
                        rs.getString("Password")
                ), uniqueString).stream().toList();

        if(!findNutzer.isEmpty())
            return Optional.of(new SimpleUser(uniqueString, "{noop}"+findNutzer.get(0).passwort(), Set.of(new SimpleRole("NUTZER"))));

        return Optional.empty();
//        return Optional.of(new SimpleUser("meomeo123@gmail.com", "{noop}AB239a", Set.of(new SimpleRole("NUTZER"))));
    }
}
