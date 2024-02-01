package de.hhu.cs.dbs.dbwk.project.persistence.sql.sqlite;

import de.hhu.cs.dbs.dbwk.project.model.Role;
import de.hhu.cs.dbs.dbwk.project.model.RoleRepository;
import de.hhu.cs.dbs.dbwk.project.model.SimpleRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository("roleRepository")
public class SqliteRoleRepository implements RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public SqliteRoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Role> findAllRoles() {
//        Set<Role> allRoles = jdbcTemplate.query("select tbl_name from sqlite_master WHERE tbl_name IN ('Nutzer', 'Buerger', 'Gaertner')",
//                (rs, rowNum) -> new SimpleRole(rs.getString("tbl_name"))).stream().collect(Collectors.toSet());
//        return allRoles;

        return Set.of(new SimpleRole("NUTZER"), new SimpleRole("BUERGER"), new SimpleRole("GARTNER"));
    }
}
