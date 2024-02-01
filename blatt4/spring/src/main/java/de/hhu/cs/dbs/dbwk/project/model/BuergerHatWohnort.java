package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record BuergerHatWohnort(@NonNull @JsonProperty("email") String email,
                                @NonNull @JsonProperty("wohnortid") int wohnortid) {
}
