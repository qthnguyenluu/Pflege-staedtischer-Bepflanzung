package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record BewertungDTO(@NonNull @JsonProperty("pflegemassnahmeid") int pflegemassnahmeid,
                           @NonNull @JsonProperty("bewertungid") int bewertungid,
                           @NonNull @JsonProperty("gaertnerid") int gaertnerid) {
}
