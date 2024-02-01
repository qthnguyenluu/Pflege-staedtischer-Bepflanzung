package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record Pflegeart(@NonNull @JsonProperty("paid") int paid,
                        @NonNull @JsonProperty("name") String name) {
}
