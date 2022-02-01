package safro.archon.api;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import safro.archon.registry.ComponentsRegistry;
import safro.archon.registry.EffectRegistry;
import safro.archon.util.ArchonUtil;

public class ManaComponent implements AutoSyncedComponent, ServerTickingComponent {
    private final PlayerEntity player;
    private int mana = 1;
    private int maxMana = 100;
    private int regenSpeed = 20;
    private int manaTickTimer;

    public ManaComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void serverTick() {
        if (this.mana < 0) {
            this.mana = 1;
        } else if (this.mana < this.maxMana) {
            ++this.manaTickTimer;
            if (this.manaTickTimer >= regenSpeed) {
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
        if (nbt.contains("mana", 99)) {
            this.mana = nbt.getInt("mana");
            this.manaTickTimer = nbt.getInt("manaTickTimer");
            this.maxMana = nbt.getInt("maxMana");
            this.regenSpeed = nbt.getInt("regenSpeed");
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putInt("mana", this.mana);
        nbt.putInt("manaTickTimer", this.manaTickTimer);
        nbt.putFloat("maxMana", this.maxMana);
        nbt.putFloat("regenSpeed", this.regenSpeed);
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

    public void addMana(int mana) {
        if (this.getMana() + mana > this.getMaxMana()) {
            this.setMana(this.getMaxMana());
        } else {
            this.mana = this.mana + mana;
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
        this.maxMana = amount;
        ComponentsRegistry.MANA_COMPONENT.sync(player);
    }

    public void setRegenSpeed(int amount) {
        this.regenSpeed = amount;
        ComponentsRegistry.MANA_COMPONENT.sync(player);
    }

    public int getMana() {
        return this.mana;
    }

    public int getMaxMana() {
        return this.maxMana;
    }

    public int getRegenSpeed() {
        return this.regenSpeed;
    }
}
