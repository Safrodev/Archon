package safro.archon.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;
import safro.archon.entity.OmegaSkeltEntity;
import safro.archon.entity.PrimeSkeltEntity;
import safro.archon.entity.projectile.IceBallEntity;
import safro.archon.entity.ManaLeechEntity;
import safro.archon.entity.SkeltEntity;
import safro.archon.entity.projectile.WaterBoltEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntityRegistry {
    private static final Map<EntityType<?>, Identifier> ENTITY_TYPES = new LinkedHashMap<>();

    public static final EntityType<WaterBoltEntity> WATER_BOLT = register("water_bolt", FabricEntityTypeBuilder.<WaterBoltEntity>create(SpawnGroup.MISC, WaterBoltEntity::new).dimensions(EntityDimensions.fixed(0.25F, 0.25F)).trackRangeBlocks(4).trackedUpdateRate(10).build());
    public static final EntityType<IceBallEntity> ICE_BALL = register("ice_ball", FabricEntityTypeBuilder.<IceBallEntity>create(SpawnGroup.MISC, IceBallEntity::new).dimensions(EntityDimensions.fixed(0.25F, 0.25F)).trackRangeBlocks(4).trackedUpdateRate(10).build());

    public static final EntityType<SkeltEntity> SKELT = register("skelt", FabricEntityTypeBuilder.<SkeltEntity>create(SpawnGroup.MISC, SkeltEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.99F)).trackRangeBlocks(8).build());
    public static final EntityType<PrimeSkeltEntity> PRIME_SKELT = register("prime_skelt", FabricEntityTypeBuilder.<PrimeSkeltEntity>create(SpawnGroup.MISC, PrimeSkeltEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.99F)).trackRangeBlocks(8).build());
    public static final EntityType<OmegaSkeltEntity> OMEGA_SKELT = register("omega_skelt", FabricEntityTypeBuilder.<OmegaSkeltEntity>create(SpawnGroup.MISC, OmegaSkeltEntity::new).dimensions(EntityDimensions.fixed(1.2F, 3.98F)).trackRangeBlocks(8).build());

    public static final EntityType<ManaLeechEntity> MANA_LEECH = register("mana_leech", FabricEntityTypeBuilder.<ManaLeechEntity>create(SpawnGroup.MONSTER, ManaLeechEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.3F)).trackRangeBlocks(8).build());

    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
        ENTITY_TYPES.put(type, new Identifier(Archon.MODID, name));
        return type;
    }

    public static void init() {
        ENTITY_TYPES.keySet().forEach(entityType -> Registry.register(Registry.ENTITY_TYPE, ENTITY_TYPES.get(entityType), entityType));

        FabricDefaultAttributeRegistry.register(SKELT, SkeltEntity.createSkeltAttributes());
        FabricDefaultAttributeRegistry.register(PRIME_SKELT, PrimeSkeltEntity.createPrimeSkeltAttributes());
        FabricDefaultAttributeRegistry.register(OMEGA_SKELT, OmegaSkeltEntity.createOmegaSkeltAttributes());
        FabricDefaultAttributeRegistry.register(MANA_LEECH, ManaLeechEntity.createLeechAttributes());
    }
}
