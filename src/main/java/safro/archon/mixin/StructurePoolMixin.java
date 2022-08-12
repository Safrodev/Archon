package safro.archon.mixin;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.Archon;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Mixin(StructurePool.class)
public class StructurePoolMixin {

    @Shadow @Final private ObjectArrayList<StructurePoolElement> elements;

    @Shadow @Final private List<Pair<StructurePoolElement, Integer>> elementCounts;

    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/util/Identifier;Lnet/minecraft/util/Identifier;Ljava/util/List;Lnet/minecraft/structure/pool/StructurePool$Projection;)V")
    private void addWizardVillageHouse(Identifier id, Identifier terminatorsId, List<Pair<Function<StructurePool.Projection, ? extends StructurePoolElement>, Integer>> elementCounts, StructurePool.Projection projection, CallbackInfo ci) {
        if (Objects.equals(id.getPath(), "village/plains/houses")) {
            add("archon:village/plains/houses/wizard_plains", projection);
        } else if (Objects.equals(id.getPath(), "village/desert/houses")) {
            add("archon:village/plains/houses/wizard_desert", projection);
        } else if (Objects.equals(id.getPath(), "village/savanna/houses")) {
            add("archon:village/plains/houses/wizard_savanna", projection);
        } else if (Objects.equals(id.getPath(), "village/snowy/houses")) {
            add("archon:village/plains/houses/wizard_snowy", projection);
        } else if (Objects.equals(id.getPath(), "village/taiga/houses")) {
            add("archon:village/plains/houses/wizard_taiga", projection);
        }
    }

    private void add(String element, StructurePool.Projection projection) {
        int weight = Archon.CONFIG.wizard_village_weight;
        StructurePoolElement e = StructurePoolElement.ofLegacySingle(element).apply(projection);
        elementCounts.add(new Pair<>(e, weight));
        for (int i = 0; i < weight; i++) {
            elements.add(e);
        }
    }
}
