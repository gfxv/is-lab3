package dev.gfxv.lab2.dao.enums;

import com.fasterxml.jackson.annotation.JsonValue;


public enum Weapon {
    MELTAGUN("Meltagun"),
    BOLT_PISTOL("Bolt Pistol"),
    GRENADE_LAUNCHER("Grenade Launcher"),
    MULTI_MELTA("Multi Melta"),
    MISSILE_LAUNCHER("Missile Launcher");

    private final String name;

    Weapon(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
