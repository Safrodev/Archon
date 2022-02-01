package safro.archon.api;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import safro.archon.registry.ComponentsRegistry;

public class ScrollComponent implements AutoSyncedComponent {
    private final PlayerEntity holder;
    private String scroll = "none";

    public ScrollComponent(PlayerEntity holder) {
        this.holder = holder;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        setScroll(tag.getString("scroll"));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (hasScroll()) {
            tag.putString("scroll", scroll);
        }
    }

    public String getScroll() {
        return scroll;
    }

    public void setScroll(String name) {
        this.scroll = name;
        ComponentsRegistry.SCROLL_COMPONENT.sync(holder);
    }

    public boolean hasScroll() {
        return !getScroll().equals("none");
    }
}
