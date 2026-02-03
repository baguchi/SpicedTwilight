package baguchi.spiced_twilight.item;

import baguchi.spiced_twilight.SpicedTwilight;
import baguchi.spiced_twilight.entity.ModEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, SpicedTwilight.MODID);

    public static final Supplier<Item> MAZE_SLIME_BEETLE_SPAWN_EGG = ITEMS.register("maze_slime_beetle_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.MAZE_SLIME_BEETLE, 10724259, 2767639, (new Item.Properties())));
}
