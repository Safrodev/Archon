package safro.archon.api;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import safro.archon.registry.ComponentsRegistry;
import safro.archon.registry.EffectRegistry;
import safro.archon.util.ArchonUtil;

import java.util.UUID;

public class ManaComponent implements AutoSyncedComponent, ServerTickingComponent {
    private final PlayerEntity player;
    private int mana = 1;
    private int manaTickTimer;
    private int regenSpeed = 20;

    public ManaComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void serverTick() {
        if (this.getMana() < 0) {
            this.setMana(1);
        } else if (this.getMana() < this.getMaxMana()) {
            ++this.manaTickTimer;
            if (this.manaTickTimer >= this.getRegenSpeed()) {
                ++this.mana;
                this.manaTickTimer = 0;
                ComponentsRegistry.MANA_COMPONENT.sync(player);
            }
        } else {
            this.manaTickTimer = 0;
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        this.mana = nbt.getInt("mana");
        this.manaTickTimer = nbt.getInt("manaTickTimer");
        this.regenSpeed = nbt.getInt("regenSpeed");
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putInt("mana", this.mana);
        nbt.putInt("manaTickTimer", this.manaTickTimer);
        nbt.putInt("regenSpeed", this.regenSpeed);
    }

    /**
     * Sets the Player's mana regen speed to the default for the player
     * Defaults to 20, but returns 10 if the player has an Ancient Scroll (Accelerate)
     */
    public void setDefaultRegenSpeed() {
        int x = 20;
        if (ArchonUtil.getScroll(player) != null && ArchonUtil.getScroll(player).equals("accelerate")) {
            x = 10;
        }
        this.setRegenSpeed(x);
    }

    /**
     * Used for item abilities and other checks that require removing mana
     *
     * @param amount - The amount of mana to be used/removed
     * @return - Returns if the player can successfully use mana of the given amount
     */
    public boolean canUseMana(int amount) {
        if (player.hasStatusEffect(EffectRegistry.OBSTRUCTED)) {
            return false;
        }
        return getMana() >= amount;
    }

    public void clampMana() {
        if (this.getMana() > this.getMaxMana()) {
            this.setMana(this.getMaxMana());
        }
    }

    public void addMana(int mana) {
        if (this.getMana() + mana > this.getMaxMana()) {
            this.setMana(this.getMaxMana());
        } else {
            this.mana += mana;
        }
        ComponentsRegistry.MANA_COMPONENT.sync(player);
    }

    public void removeMana(int mana) {
        this.mana = this.mana - mana;
        ComponentsRegistry.MANA_COMPONENT.sync(player);
    }

    public void setMana(int amount) {
        this.mana = amount;
        ComponentsRegistry.MANA_COMPONENT.sync(player);
    }

    public void setMaxMana(int amount) {
        player.getAttributeInstance(ManaAttributes.MAX_MANA).setBaseValue(amount);
        this.clampMana();
        ComponentsRegistry.MANA_COMPONENT.sync(player);
    }

    public void setRegenSpeed(int amount) {
        if (amount < 0) amount = 1;
        this.regenSpeed = amount;
        ComponentsRegistry.MANA_COMPONENT.sync(player);
    }

    public void removeMaxModifier(UUID id) {
        if (player.getAttributeInstance(ManaAttributes.MAX_MANA).getModifier(id) != null) {
            player.getAttributeInstance(ManaAttributes.MAX_MANA).removeModifier(id);
        }
    }

    public void addMaxModifier(UUID id, String name, double amt, boolean temp) {
        EntityAttributeModifier modifier = new EntityAttributeModifier(id, name, amt, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        EntityAttributeInstance instance = player.getAttributeInstance(ManaAttributes.MAX_MANA);
        if (instance.hasModifier(modifier)) return;

        if (temp) {
            instance.addTemporaryModifier(new EntityAttributeModifier(id, name, amt, EntityAttributeModifier.Operation.ADDITION));
        } else
            instance.addPersistentModifier(new EntityAttributeModifier(id, name, amt, EntityAttributeModifier.Operation.ADDITION));
    }

    public int getMana() {
        return this.mana;
    }

    public int getMaxMana() {
        return (int)player.getAttributeValue(ManaAttributes.MAX_MANA);
    }

    public int getRegenSpeed() {
        return this.regenSpeed;
    }
}
