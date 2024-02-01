package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record NutzerDAO(@NonNull @JsonProperty("email") String email,
                        @NonNull @JsonProperty("passwort") String passwort) {
}
