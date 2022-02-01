package safro.archon.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.api.ManaComponent;
import safro.archon.api.ScrollComponent;

public class ComponentsRegistry implements EntityComponentInitializer {
    public static final ComponentKey<ManaComponent> MANA_COMPONENT = ComponentRegistry.getOrCreate(new Identifier(Archon.MODID, "mana"), ManaComponent.class);
    public static final ComponentKey<ScrollComponent> SCROLL_COMPONENT = ComponentRegistry.getOrCreate(new Identifier(Archon.MODID, "scroll"), ScrollComponent.class);


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(SCROLL_COMPONENT, ScrollComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(MANA_COMPONENT, ManaComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
