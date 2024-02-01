package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;

public record PflanzeDTO(@NonNull @JsonProperty("pflanzeid") int pflanzeid,
                         @NonNull @JsonProperty("lname") String lname,
                         @NonNull @JsonProperty("dname") String dname,
                         @NonNull @JsonProperty("laengengrad") double laengengrad,
                         @NonNull @JsonProperty("breitengrad") double breitengrad,
                         @NonNull @JsonProperty("pflanzentyp") String pflanzentyp,
                         @NonNull @JsonProperty("datum") String datum) {
}
