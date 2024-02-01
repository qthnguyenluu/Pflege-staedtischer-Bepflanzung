package de.hhu.cs.dbs.dbwk.project.persistence.inmemory;

import de.hhu.cs.dbs.dbwk.project.model.*;
import de.hhu.cs.dbs.dbwk.project.persistence.PersistenceConfiguration;

import java.util.Collections;
import java.util.Set;

public class InMemoryPersistenceConfiguration implements PersistenceConfiguration {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public InMemoryPersistenceConfiguration() {
        roleRepository = new InMemoryRoleRepository(roles());
        userRepository = new InMemoryUserRepository(users());
    }

    @Override
    public RoleRepository roleRepository() {
        return roleRepository;
    }

    private Set<Role> roles() {
        Role user = new SimpleRole("USER", Collections.emptySet());
        Role employee = new SimpleRole("EMPLOYEE", Set.of(user));
        Role admin = new SimpleRole("ADMIN", Set.of(employee));
        return Set.of(user, employee, admin);
    }

    @Override
    public UserRepository userRepository() {
        return userRepository;
    }

    private Set<User> users() {
        User foo =
                new SimpleUser(
                        "foo", "123", Set.of(new SimpleRole("USER", Collections.emptySet())));
        User bar =
                new SimpleUser(
                        "bar", "asd", Set.of(new SimpleRole("EMPLOYEE", Collections.emptySet())));
        return Set.of(foo, bar);
    }
}
