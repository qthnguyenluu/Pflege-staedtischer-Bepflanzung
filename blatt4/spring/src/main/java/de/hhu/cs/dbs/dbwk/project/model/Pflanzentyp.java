package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record Pflanzentyp(@NonNull @JsonProperty("tid") int tid,
                          @NonNull @JsonProperty("bezeichnung") String bezeichnung) {
}
