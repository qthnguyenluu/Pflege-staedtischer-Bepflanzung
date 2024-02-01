package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record Bild(@NonNull @JsonProperty("id") int id,
                   @NonNull @JsonProperty("pfad") String pfad) {
}
