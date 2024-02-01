package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record BuergerDTO(@NonNull @JsonProperty("nutzerid") int nutzerid,
                         @NonNull @JsonProperty("buergerid") int buergerid,
                         @NonNull @JsonProperty("adresseid") int adresseid,
                         @NonNull @JsonProperty("email") String email,
                         @NonNull @JsonProperty("passwort") String passwort,
                         @NonNull @JsonProperty("vorname") String vorname,
                         @NonNull @JsonProperty("nachname") String nachname) {
}
