package safro.archon.api;

import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

import java.util.UUID;

public enum Element {
    FIRE(SpellSchools.FIRE, UUID.fromString("e92c1e09-c9f4-4ee8-bba5-9bef41115b63")),
    WATER(SpellSchools.FROST, UUID.fromString("9bbc9be8-6de5-4451-af57-3f767cfdebff")),
    EARTH(ArchonSchools.EARTH, UUID.fromString("6b31907b-3d90-4f05-9edc-9bb03b6043f1")),
    SKY(SpellSchools.LIGHTNING, UUID.fromString("d63af5ea-e6a3-4469-8531-c7b317868b95")),
    END(ArchonSchools.VOID, UUID.fromString("3a926937-80f7-4122-b215-c47db53c8052"));

    private final SpellSchool school;
    private final UUID uuid;

    Element(SpellSchool school, UUID uuid) {
        this.school = school;
        this.uuid = uuid;
    }

    public SpellSchool getSchool() {
        return this.school;
    }

    public int getColor() {
        return this.school.color;
    }

    public UUID getAttributeUUID() {
        return this.uuid;
    }
}
