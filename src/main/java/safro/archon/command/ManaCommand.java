package safro.archon.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import safro.archon.api.ManaAttributes;
import safro.archon.util.ArchonUtil;

import java.util.Collection;

public class ManaCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        int cap = (int)ManaAttributes.MAX_MANA.getMaxValue();

        dispatcher.register(CommandManager.literal("mana")
                .requires(source -> source.hasPermissionLevel(2))

                .then(CommandManager.literal("add")
                    .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                            .executes(context -> executeAdd(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"))))))

                .then(CommandManager.literal("setMax")
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .then(CommandManager.argument("amount", IntegerArgumentType.integer(1, cap))
                                        .executes(context -> executeMax(context.getSource(), EntityArgumentType.getPlayers(context, "targets"), IntegerArgumentType.getInteger(context, "amount"))))))
        );
    }

    private static int executeAdd(ServerCommandSource source, Collection<ServerPlayerEntity> players, int amount) {
        for (ServerPlayerEntity player : players) {
            ArchonUtil.get(player).addMana(amount);
        }

        if (players.size() == 1) {
            source.sendFeedback(new TranslatableText("command.archon.mana_add_one", amount, players.iterator().next().getDisplayName()), true);
        } else {
            source.sendFeedback(new TranslatableText("command.archon.mana_add_multiple", amount, players.size()), true);
        }
        return players.size();
    }

    private static int executeMax(ServerCommandSource source, Collection<ServerPlayerEntity> players, int amount) {
        for (ServerPlayerEntity player : players) {
            ArchonUtil.get(player).setMaxMana(amount);
        }

        if (players.size() == 1) {
            source.sendFeedback(new TranslatableText("command.archon.mana_set_one", amount, players.iterator().next().getDisplayName()), true);
        } else {
            source.sendFeedback(new TranslatableText("command.archon.mana_set_multiple", amount, players.size()), true);
        }
        return players.size();
    }
}
