package baguchi.spiced_twilight.entity;

import baguchi.spiced_twilight.SpicedTwilight;
import baguchi.spiced_twilight.entity.projectile.MazeSlimeBallProjectile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
@EventBusSubscriber(modid = SpicedTwilight.MODID)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, SpicedTwilight.MODID);

    public static final Supplier<EntityType<MazeSlimeBallProjectile>> MAZE_SLIME_BALL = ENTITIES.register("maze_slime_ball", () -> EntityType.Builder.<MazeSlimeBallProjectile>of(MazeSlimeBallProjectile::new, MobCategory.MISC)
            .sized(0.35F, 0.35F).eyeHeight(0.325F).updateInterval(20).build(SpicedTwilight.MODID + ":maze_slime_ball"));
    public static final Supplier<EntityType<MazeSlimeBeetle>> MAZE_SLIME_BEETLE = ENTITIES.register("maze_slime_beetle", () -> EntityType.Builder.of(MazeSlimeBeetle::new, MobCategory.MONSTER).sized(0.9F, 0.5F).build(SpicedTwilight.MODID + ":maze_slime_beetle"));


    @SubscribeEvent
    public static void registerSpawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(MAZE_SLIME_BEETLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
    }

    @SubscribeEvent
    public static void registerEntityAttribute(EntityAttributeCreationEvent event) {
        event.put(MAZE_SLIME_BEETLE.get(), MazeSlimeBeetle.registerAttributes().build());
    }
}