package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record Spezialgebiet(@NonNull @JsonProperty("spid") int spid,
                            @NonNull @JsonProperty("bezeichnung") String bezeichnung) {
}
