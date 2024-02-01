package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record PflegeprotokollDTO(@NonNull @JsonProperty("pflegemassnahmen_anzahl") int pflegemassnahmen_anzahl,
                                 @NonNull @JsonProperty("gaertnerid") int gaertnerid) {
}
