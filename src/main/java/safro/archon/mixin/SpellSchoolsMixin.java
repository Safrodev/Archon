package safro.archon.mixin;

import net.spell_power.api.SpellSchools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.api.ArchonSchools;

@Mixin(value = SpellSchools.class)
public class SpellSchoolsMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void archonSchools(CallbackInfo ci) {
        ArchonSchools.register();
    }
}
