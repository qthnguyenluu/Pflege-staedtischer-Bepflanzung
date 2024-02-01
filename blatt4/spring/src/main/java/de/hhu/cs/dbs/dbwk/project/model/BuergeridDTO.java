package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record BuergeridDTO(@NonNull @JsonProperty("buergerid") int buergerid,
                           @NonNull @JsonProperty("pflegemassnahmeid") int pflegemassnahmeid) {
}
