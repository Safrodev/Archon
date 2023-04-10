package safro.archon.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.mixin.StructurePoolAccessor;

import java.util.ArrayList;
import java.util.List;

public class WizardVillagePool {

    public static void register(MinecraftServer server) {
        add(server, "village/plains/houses", "archon:village/plains/houses/wizard_plains");
        add(server, "village/desert/houses", "archon:village/plains/houses/wizard_desert");
        add(server, "village/savanna/houses", "archon:village/plains/houses/wizard_savanna");
        add(server, "village/snowy/houses", "archon:village/plains/houses/wizard_snowy");
        add(server, "village/taiga/houses", "archon:village/plains/houses/wizard_taiga");
    }

    private static void add(MinecraftServer server, String village, String structure) {
        int weight = Archon.CONFIG.wizard_village_weight;
        Registry<StructurePool> template = server.getRegistryManager().get(RegistryKeys.TEMPLATE_POOL);
        StructurePool pool = template.get(new Identifier(village));

        if (pool != null) {
            SinglePoolElement element = SinglePoolElement.ofLegacySingle(structure).apply(StructurePool.Projection.RIGID);
            for (int i = 0; i < weight; i++) {
                ((StructurePoolAccessor) pool).getElements().add(element);
            }
            List<Pair<StructurePoolElement, Integer>> list = new ArrayList<>(((StructurePoolAccessor) pool).getElementCounts());
            list.add(new Pair<>(element, weight));
            ((StructurePoolAccessor) pool).setElementCounts(list);
        }
    }
}
