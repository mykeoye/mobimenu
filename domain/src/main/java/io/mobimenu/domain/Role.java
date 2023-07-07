package io.mobimenu.domain;

import java.util.Set;

/**
 * Represents a Role on the system, which defines what a user can or cannot do
 */
public record Role (
        RoleId id,
        String name,
        RoleType type,
        Set<Permission> permissions) {

    Role(String name, RoleType type, Set<Permission> permissions) {
        this(null, name, type, Set.copyOf(permissions));
    }

    /**
     * The type of the role
     */
    enum RoleType {
        SYSTEM_DEFINED, USER_DEFINED, DEFAULT
    }

    /**
     * Unique identifier for the role
     */
    record RoleId(String roleId) {
    }

}
