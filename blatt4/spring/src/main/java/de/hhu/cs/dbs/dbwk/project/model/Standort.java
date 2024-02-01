package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record Standort(@NonNull @JsonProperty("stid") int stid,
                       @NonNull @JsonProperty("breitengrad") double breitengrad,
                       @NonNull @JsonProperty("laengengrad") double laengengrad) {
}
