package safro.archon.api;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class ScrollComponent implements AutoSyncedComponent {
    private final PlayerEntity player;
    private String scroll = "none";

    public ScrollComponent(PlayerEntity holder) {
        this.player = holder;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        setScroll(tag.getString("scroll"));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putString("scroll", getScroll());
    }

    public String getScroll() {
        return this.scroll;
    }

    public void setScroll(String name) {
        this.scroll = name;
    }

    public boolean hasScroll() {
        return !getScroll().equals("none");
    }
}
