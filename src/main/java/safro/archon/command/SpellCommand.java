package safro.archon.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import safro.archon.api.spell.Spell;
import safro.archon.registry.ComponentsRegistry;
import safro.archon.registry.SpellRegistry;
import safro.archon.util.ArchonUtil;

import java.util.ArrayList;
import java.util.List;

public class SpellCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(CommandManager.literal("spells")
                .then(CommandManager.literal("list")
                    .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> getAllSpells(context.getSource(), EntityArgumentType.getPlayer(context, "target")))))

                .then(CommandManager.literal("remove")
                    .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                            .then(CommandManager.argument("spell", RegistryEntryArgumentType.registryEntry(access, SpellRegistry.REGISTRY_KEY))
                                .executes(context -> removeSpell(context.getSource(), EntityArgumentType.getPlayer(context, "target"), RegistryEntryArgumentType.getRegistryEntry(context, "spell", SpellRegistry.REGISTRY_KEY))))))
        );
    }

    private static int getAllSpells(ServerCommandSource source, ServerPlayerEntity player) {
        List<Spell> spells = ArchonUtil.getSpells(player);
        if (spells.size() <= 0) {
            source.sendFeedback(() -> Text.translatable("command.archon.spell.no_spells"), false);
        } else {
            for (Spell s : spells) {
                source.sendFeedback(() -> Text.translatable(s.getTranslationKey()).setStyle(Style.EMPTY.withColor(s.getElement().getColor())), false);
            }
        }
        return spells.size();
    }

    private static int removeSpell(ServerCommandSource source, ServerPlayerEntity player, RegistryEntry.Reference<Spell> reference) {
        Spell spell = reference.value();
        List<Spell> spells = ArchonUtil.getSpells(player);
        if (!spells.contains(spell)) {
            source.sendFeedback(() -> Text.translatable("command.archon.spell.not_found"), false);
            return 0;
        } else {
            List<Spell> modified = new ArrayList<>(spells);
            modified.remove(spell);
            ComponentsRegistry.SPELL_COMPONENT.get(player).setSpells(modified);
            source.sendFeedback(() -> Text.translatable("command.archon.spell.removed", player.getDisplayName().getString()), true);
            return 1;
        }
    }
}
