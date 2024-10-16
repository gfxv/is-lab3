package dev.gfxv.lab1.dao.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
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
