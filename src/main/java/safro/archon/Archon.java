package safro.archon;

import draylar.omegaconfig.OmegaConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import safro.archon.api.ManaAttributes;
import safro.archon.command.ManaCommand;
import safro.archon.command.SpellCommand;
import safro.archon.config.ArchonConfig;
import safro.archon.network.NetworkManager;
import safro.archon.registry.*;
import safro.archon.world.WizardVillagePool;
import safro.saflib.SafLib;

import java.util.ArrayList;

public class Archon implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("archon");
	public static final String MODID = "archon";
	public static final ArrayList<ItemStack> ITEMS = new ArrayList<>();
	public static RegistryKey<ItemGroup> ITEMGROUP = SafLib.createGroup(MODID);
	public static SoundRegistry SOUNDS;
	public static final ArchonConfig CONFIG = OmegaConfig.register(ArchonConfig.class);

	@Override
	public void onInitialize() {
		// Setup
		NetworkManager.initServer();
		ManaAttributes.init();

		// Init Content
		EntityRegistry.init();
		ItemRegistry.init();
		BlockRegistry.init();
		SpellRegistry.init();
		TagRegistry.init();
		EffectRegistry.init();
		MiscRegistry.init();
		ParticleRegistry.init();
		RecipeRegistry.init();
		CriteriaRegistry.init();
		WorldRegistry.init();
		VillagerRegistry.init();
		LootTableRegistry.init();

		// Events
		ComponentsRegistry.initEvents();
		ServerLifecycleEvents.SERVER_STARTING.register(WizardVillagePool::register);

		// Init Commands
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, env) -> {
			ManaCommand.register(dispatcher);
			SpellCommand.register(dispatcher, dedicated);
		});

		// Init Sounds
		SOUNDS = new SoundRegistry();

		SafLib.registerAll(ITEMGROUP, ItemRegistry.FIRE_ESSENCE);
	}
}
