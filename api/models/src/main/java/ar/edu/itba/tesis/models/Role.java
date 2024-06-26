package ar.edu.itba.tesis.models;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    MODERATOR("ROLE_MODERATOR"),
    DETECTOR("ROLE_DETECTOR"),
    USER("ROLE_USER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

}
