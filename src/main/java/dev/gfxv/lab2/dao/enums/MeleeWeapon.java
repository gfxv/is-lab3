package dev.gfxv.lab2.dao.enums;

import com.fasterxml.jackson.annotation.JsonValue;


public enum MeleeWeapon {
    CHAIN_SWORD("Chain Sword"),
    MANREAPER("Manreaper"),
    POWER_BLADE("Power Blade"),
    POWER_FIST("Power Fist");

    private final String name;

    MeleeWeapon(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
