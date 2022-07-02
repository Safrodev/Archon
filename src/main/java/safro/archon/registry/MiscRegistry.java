package safro.archon.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;
import safro.archon.client.screen.ScriptureTableScreenHandler;
import safro.archon.enchantment.ArcaneEnchantment;

public class MiscRegistry {
    // Screen Handlers
    public static ScreenHandlerType<ScriptureTableScreenHandler> SCRIPTURE_TABLE_SH;

    // Enchantments
    public static Enchantment ARCANE;

    public static void init() {
        SCRIPTURE_TABLE_SH = ScreenHandlerRegistry.registerSimple(new Identifier(Archon.MODID, "scripture_table"), ScriptureTableScreenHandler::new);

        ARCANE = Registry.register(Registry.ENCHANTMENT, new Identifier(Archon.MODID, "arcane"), new ArcaneEnchantment());
    }
}
