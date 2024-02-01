package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record WohnortDTO(
        @NonNull @JsonProperty("adresseid") int adresseid,
        @NonNull @JsonProperty("strasse") String strasse,
        @NonNull @JsonProperty("hausnummer") String hausnummer,
        @NonNull @JsonProperty("plz") String plz,
        @NonNull @JsonProperty("stadt") String stadt) {
}

