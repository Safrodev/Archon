package safro.archon.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import safro.archon.Archon;
import safro.archon.client.screen.ExperiencePouchScreenHandler;
import safro.archon.client.screen.ScriptureTableScreenHandler;
import safro.archon.enchantment.ArcaneEnchantment;
import safro.archon.enchantment.ReapingEnchantment;

public class MiscRegistry {
    // Screen Handlers
    public static ScreenHandlerType<ScriptureTableScreenHandler> SCRIPTURE_TABLE_SH;
    public static ScreenHandlerType<ExperiencePouchScreenHandler> EXPERIENCE_POUCH_SH;

    // Enchantments
    public static Enchantment ARCANE;
    public static Enchantment REAPING;

    public static void init() {
        SCRIPTURE_TABLE_SH = ScreenHandlerRegistry.registerSimple(new Identifier(Archon.MODID, "scripture_table"), ScriptureTableScreenHandler::new);
        EXPERIENCE_POUCH_SH = ScreenHandlerRegistry.registerSimple(new Identifier(Archon.MODID, "experience_pouch"), ExperiencePouchScreenHandler::new);

        ARCANE = Registry.register(Registries.ENCHANTMENT, new Identifier(Archon.MODID, "arcane"), new ArcaneEnchantment());
        REAPING = Registry.register(Registries.ENCHANTMENT, new Identifier(Archon.MODID, "reaping"), new ReapingEnchantment());
    }
}
