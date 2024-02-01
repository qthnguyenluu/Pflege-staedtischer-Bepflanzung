package de.hhu.cs.dbs.dbwk.project.persistence.inmemory;

import de.hhu.cs.dbs.dbwk.project.model.Role;
import de.hhu.cs.dbs.dbwk.project.model.SimpleRole;
import de.hhu.cs.dbs.dbwk.project.model.SimpleUser;
import de.hhu.cs.dbs.dbwk.project.model.User;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class InMemoryConfiguration {

    @ConditionalOnBean(InMemoryRoleRepository.class)
    @Bean
    Set<Role> roles() {
        Role user = new SimpleRole("USER");
        Role employee = new SimpleRole("EMPLOYEE");
        Role admin = new SimpleRole("ADMIN");
        return Set.of(user, employee, admin);
    }

    @ConditionalOnBean(InMemoryUserRepository.class)
    @Bean
    Set<User> users() {
        User foo = new SimpleUser("foo", "{noop}123", Set.of(new SimpleRole("USER")));
        User bar =
                new SimpleUser(
                        "bar",
                        "{noop}asd",
                        Set.of(new SimpleRole("USER"), new SimpleRole("EMPLOYEE")));
        return Set.of(foo, bar);
    }
}
