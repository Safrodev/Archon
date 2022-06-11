package safro.archon.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;
import safro.archon.client.screen.ScriptureTableScreenHandler;
import safro.archon.enchantment.ArcaneEnchantment;
import safro.archon.util.criterion.ChanneledCriterion;
import safro.archon.util.criterion.SummonUndeadCriterion;

public class MiscRegistry {
    // Screen Handlers
    public static ScreenHandlerType<ScriptureTableScreenHandler> SCRIPTURE_TABLE_SH;

    // Enchantments
    public static Enchantment ARCANE;

    // Criterion
    public static SummonUndeadCriterion SUMMON_UNDEAD_CRITERION;
    public static ChanneledCriterion CHANNELED_CRITERION;

    public static void init() {
        SCRIPTURE_TABLE_SH = ScreenHandlerRegistry.registerSimple(new Identifier(Archon.MODID, "scripture_table"), ScriptureTableScreenHandler::new);

        SUMMON_UNDEAD_CRITERION = CriteriaAccessor.callRegister(new SummonUndeadCriterion());
        CHANNELED_CRITERION = CriteriaAccessor.callRegister(new ChanneledCriterion());

        ARCANE = Registry.register(Registry.ENCHANTMENT, new Identifier(Archon.MODID, "arcane"), new ArcaneEnchantment());
    }
}
