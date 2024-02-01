package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record PflanzeBilderDTO(@NonNull @JsonProperty("pflanzeid") int pflanzeid,
                               @NonNull @JsonProperty("pfad") String pfad) {
}
