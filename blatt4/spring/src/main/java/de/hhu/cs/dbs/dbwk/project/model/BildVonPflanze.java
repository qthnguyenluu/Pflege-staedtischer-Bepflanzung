package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record BildVonPflanze(@NonNull @JsonProperty("pflanzeid") int pflanzeid,
                             @NonNull @JsonProperty("bildid") int bildid) {
}
