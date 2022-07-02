package safro.archon;

import draylar.omegaconfig.OmegaConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import safro.archon.api.ManaAttributes;
import safro.archon.api.Spell;
import safro.archon.command.ManaCommand;
import safro.archon.config.ArchonConfig;
import safro.archon.network.NetworkManager;
import safro.archon.registry.*;

public class Archon implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("archon");
	public static final String MODID = "archon";
	public static ItemGroup ITEMGROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(ItemRegistry.ENDER_BLADE));
	public static SoundRegistry SOUNDS;
	public static final ArchonConfig CONFIG = OmegaConfig.register(ArchonConfig.class);

	// Registries
	public static final Registry<Spell> SPELL = FabricRegistryBuilder.createSimple(Spell.class, new Identifier(MODID, "spell")).buildAndRegister();

	@Override
	public void onInitialize() {
		// Setup
		NetworkManager.initServer();
		ManaAttributes.init();

		// Init Content
		EntityRegistry.init();
		ItemRegistry.init();
		BlockRegistry.init();
		TagRegistry.init();
		WorldRegistry.init();
		VillagerRegistry.init();
		EffectRegistry.init();
		MiscRegistry.init();
		RecipeRegistry.init();
		SpellRegistry.init();
		CriteriaRegistry.init();

		// Init Commands
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			ManaCommand.register(dispatcher);
		});

		// Init Sounds
		SOUNDS = new SoundRegistry();
	}
}
