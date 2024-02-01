package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record PflegemassnahmeDTO(@NonNull @JsonProperty("pflegemassnahmeid") int pflegemassnahmeid,
                                 @NonNull @JsonProperty("avg_bewertung") double avg_bewertung,
                                 @NonNull @JsonProperty("datum") String datum,
                                 @NonNull @JsonProperty("pflegeart") String pflegeart) {
}

// {
//         "pflegemassnahmeid": 0,
//         "avg_bewertung": 0.1,
//         "datum": "2024-01-16",
//         "pflegeart": "string"
//         }