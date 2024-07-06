package safro.archon.api;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import safro.archon.registry.ComponentsRegistry;
import safro.archon.registry.EffectRegistry;

import java.util.UUID;

public class ManaComponent implements AutoSyncedComponent, ServerTickingComponent {
    private final PlayerEntity player;
    private int mana = 0;
    private int manaTickTimer;

    public ManaComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void serverTick() {
        this.clampMana();
        if (this.getMana() < this.getMaxMana()) {
            ++this.manaTickTimer;
            if (this.manaTickTimer >= 4) {
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
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putInt("mana", this.mana);
        nbt.putInt("manaTickTimer", this.manaTickTimer);
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
        this.setMana(MathHelper.clamp(this.getMana(), 0, this.getMaxMana()));
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

    public void removeMaxModifier(UUID id) {
        if (player.getAttributeInstance(ManaAttributes.MAX_MANA).getModifier(id) != null) {
            player.getAttributeInstance(ManaAttributes.MAX_MANA).removeModifier(id);
        }
    }

    public void addMaxModifier(UUID id, String name, double amt, boolean temp) {
        EntityAttributeModifier modifier = new EntityAttributeModifier(id, name, amt, EntityAttributeModifier.Operation.ADDITION);
        EntityAttributeInstance instance = player.getAttributeInstance(ManaAttributes.MAX_MANA);
        if (instance.hasModifier(modifier)) return;

        if (temp) {
            instance.addTemporaryModifier(modifier);
        } else
            instance.addPersistentModifier(modifier);
    }

    public int getMana() {
        return this.mana;
    }

    public int getMaxMana() {
        return (int)player.getAttributeValue(ManaAttributes.MAX_MANA);
    }
}
