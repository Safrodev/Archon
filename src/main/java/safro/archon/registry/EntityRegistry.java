package safro.archon.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import safro.archon.Archon;
import safro.archon.entity.ManaLeechEntity;
import safro.archon.entity.OmegaSkeltEntity;
import safro.archon.entity.PrimeSkeltEntity;
import safro.archon.entity.SkeltEntity;
import safro.archon.entity.boss.*;
import safro.archon.entity.projectile.IceBallEntity;
import safro.archon.entity.projectile.WaterBoltEntity;
import safro.archon.entity.projectile.WindBallEntity;
import safro.archon.entity.projectile.spell.CloudshotEntity;
import safro.archon.entity.projectile.spell.HellbeamEntity;
import safro.archon.entity.projectile.spell.SpellProjectileEntity;
import safro.archon.entity.projectile.spell.TerrainEntity;
import safro.saflib.registry.BaseEntityRegistry;

public class EntityRegistry extends BaseEntityRegistry {
    static { MODID = Archon.MODID; }

    // Projectiles
    public static final EntityType<WaterBoltEntity> WATER_BOLT = register("water_bolt", FabricEntityTypeBuilder.<WaterBoltEntity>create(SpawnGroup.MISC, WaterBoltEntity::new).dimensions(projectile()).trackRangeChunks(4).trackedUpdateRate(10).build());
    public static final EntityType<IceBallEntity> ICE_BALL = register("ice_ball", FabricEntityTypeBuilder.<IceBallEntity>create(SpawnGroup.MISC, IceBallEntity::new).dimensions(projectile()).trackRangeChunks(4).trackedUpdateRate(10).build());
    public static final EntityType<WindBallEntity> WIND_BALL = register("wind_ball", FabricEntityTypeBuilder.<WindBallEntity>create(SpawnGroup.MISC, WindBallEntity::new).dimensions(EntityDimensions.fixed(0.3125F, 0.3125F)).trackRangeChunks(4).trackedUpdateRate(10).build());

    // Spell Projectiles
    public static final EntityType<SpellProjectileEntity> SPELL_PROJECTILE = register("spell_projectile", FabricEntityTypeBuilder.<SpellProjectileEntity>create(SpawnGroup.MISC, SpellProjectileEntity::new).dimensions(projectile()).trackRangeChunks(8).trackedUpdateRate(10).build());
    public static final EntityType<HellbeamEntity> HELLBEAM = register("hellbeam", FabricEntityTypeBuilder.<HellbeamEntity>create(SpawnGroup.MISC, HellbeamEntity::new).dimensions(projectile()).trackRangeChunks(8).trackedUpdateRate(10).build());
    public static final EntityType<CloudshotEntity> CLOUDSHOT = register("cloudshot", FabricEntityTypeBuilder.<CloudshotEntity>create(SpawnGroup.MISC, CloudshotEntity::new).dimensions(projectile()).trackRangeChunks(8).trackedUpdateRate(10).build());
    public static final EntityType<TerrainEntity> TERRAIN = register("terrain", FabricEntityTypeBuilder.<TerrainEntity>create(SpawnGroup.MISC, TerrainEntity::new).dimensions(projectile()).trackRangeChunks(8).trackedUpdateRate(10).build());

    // Necromancy
    public static final EntityType<SkeltEntity> SKELT = register("skelt", FabricEntityTypeBuilder.<SkeltEntity>create(SpawnGroup.MISC, SkeltEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.99F)).trackRangeChunks(10).build());
    public static final EntityType<PrimeSkeltEntity> PRIME_SKELT = register("prime_skelt", FabricEntityTypeBuilder.<PrimeSkeltEntity>create(SpawnGroup.MISC, PrimeSkeltEntity::new).dimensions(EntityDimensions.fixed(0.6F, 1.99F)).trackRangeChunks(10).build());
    public static final EntityType<OmegaSkeltEntity> OMEGA_SKELT = register("omega_skelt", FabricEntityTypeBuilder.<OmegaSkeltEntity>create(SpawnGroup.MISC, OmegaSkeltEntity::new).dimensions(EntityDimensions.fixed(1.2F, 3.98F)).trackRangeChunks(10).build());

    // Misc
    public static final EntityType<ManaLeechEntity> MANA_LEECH = register("mana_leech", FabricEntityTypeBuilder.<ManaLeechEntity>create(SpawnGroup.MONSTER, ManaLeechEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.3F)).trackRangeChunks(8).build());

    // Bosses
    public static final EntityType<TarEntity> TAR = register("tar", FabricEntityTypeBuilder.<TarEntity>create(SpawnGroup.MISC, TarEntity::new).dimensions(player()).trackRangeChunks(15).build());
    public static final EntityType<AlyaEntity> ALYA = register("ayla", FabricEntityTypeBuilder.<AlyaEntity>create(SpawnGroup.MISC, AlyaEntity::new).dimensions(player()).trackRangeChunks(15).build());
    public static final EntityType<LevenEntity> LEVEN = register("leven", FabricEntityTypeBuilder.<LevenEntity>create(SpawnGroup.MISC, LevenEntity::new).dimensions(player()).trackRangeChunks(15).build());
    public static final EntityType<InigoEntity> INIGO = register("inigo", FabricEntityTypeBuilder.<InigoEntity>create(SpawnGroup.MISC, InigoEntity::new).dimensions(player()).trackRangeChunks(15).fireImmune().build());
    public static final EntityType<NullEntity> NULL = register("null", FabricEntityTypeBuilder.<NullEntity>create(SpawnGroup.MISC, NullEntity::new).dimensions(player()).trackRangeChunks(15).build());

    public static void init() {
        addAttributes(SKELT, SkeltEntity.createSkeltAttributes());
        addAttributes(PRIME_SKELT, PrimeSkeltEntity.createPrimeSkeltAttributes());
        addAttributes(OMEGA_SKELT, OmegaSkeltEntity.createOmegaSkeltAttributes());
        addAttributes(MANA_LEECH, ManaLeechEntity.createLeechAttributes());
        addAttributes(TAR, TarEntity.createTarAttributes());
        addAttributes(ALYA, AlyaEntity.createAlyaAttributes());
        addAttributes(LEVEN, LevenEntity.createLevenAttributes());
        addAttributes(INIGO, InigoEntity.createInigoAttributes());
        addAttributes(NULL, NullEntity.createNullAttributes());
    }

    private static EntityDimensions projectile() {
        return EntityDimensions.fixed(0.25F, 0.25F);
    }
}
