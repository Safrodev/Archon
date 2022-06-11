package safro.archon.api;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.registry.ComponentsRegistry;

import java.util.ArrayList;
import java.util.List;

public class SpellComponent implements AutoSyncedComponent {
    private final PlayerEntity player;
    private final List<Spell> spells = new ArrayList<>();

    public SpellComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("Spells")) {
            getSpells().clear();
            NbtList list = tag.getList("Spells", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < list.size(); i++) {
                NbtCompound nbt = list.getCompound(i);
                Spell s = Archon.SPELL.get(new Identifier(nbt.getString("Spell")));
                getSpells().add(s);
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList list = new NbtList();
        for (int i = 0; i < getSpells().size(); i++) {
            NbtCompound nbt = new NbtCompound();
            nbt.putString("Spell", Archon.SPELL.getId(getSpells().get(i)).toString());
            list.add(nbt);
        }
        tag.put("Spells", list);
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public boolean addSpell(Spell spell) {
        if (!getSpells().contains(spell)) {
            getSpells().add(spell);
            ComponentsRegistry.SPELL_COMPONENT.sync(player);
            return true;
        }
        return false;
    }

}
