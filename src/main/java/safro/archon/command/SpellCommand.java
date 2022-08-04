package safro.archon.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import safro.archon.api.Spell;
import safro.archon.util.ArchonUtil;

import java.util.List;

public class SpellCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("spell")
                .requires(source -> source.hasPermissionLevel(2))

                .then(CommandManager.literal("getAll")
                    .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> getAllSpells(context.getSource(), EntityArgumentType.getPlayer(context, "target")))))

        );
    }

    private static int getAllSpells(ServerCommandSource source, ServerPlayerEntity player) {
        List<Spell> spells = ArchonUtil.getSpells(player);
        if (spells.size() <= 0) {
            source.sendFeedback(new TranslatableText("command.archon.spell.no_spells"), false);
        } else {
            for (Spell s : spells) {
                source.sendFeedback(new TranslatableText(s.getTranslationKey()).setStyle(Style.EMPTY.withColor(s.getElement().getColor())), false);
            }
        }
        return spells.size();
    }
}
