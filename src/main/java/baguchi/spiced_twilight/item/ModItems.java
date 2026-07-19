package baguchi.spiced_twilight.item;

import baguchi.spiced_twilight.SpicedTwilight;
import baguchi.spiced_twilight.entity.ModEntities;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SpicedTwilight.MODID);

    public static final DeferredItem<Item> MAZE_SLIME_BEETLE_SPAWN_EGG = ITEMS.registerItem("maze_slime_beetle_spawn_egg", (properties) -> new DeferredSpawnEggItem(ModEntities.MAZE_SLIME_BEETLE, 10724259, 2767639, (properties)));
    public static final DeferredItem<Item> FIRE_BEETLE_POWDER = ITEMS.registerItem("fire_beetle_powder", (properties) -> new Item((properties.component(DataComponents.LORE, new ItemLore(List.of(Component.translatable("item.spiced_twilight.fire_beetle_powder.desc")))))));
}
