package baguchi.spiced_twilight.item;

import baguchi.spiced_twilight.SpicedTwilight;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.init.TFCreativeTabs;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SpicedTwilight.MODID);

    private static ResourceKey<CreativeModeTab> createKey(String p_281544_) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(SpicedTwilight.MODID, p_281544_));
    }

    public static final Supplier<CreativeModeTab> SPICED_TWILIGHT = CREATIVE_MODE_TABS.register("spiced_twilight", () -> CreativeModeTab.builder()
            .withTabsBefore(TFCreativeTabs.ITEMS.getKey())
            .title(Component.translatable("itemGroup." + SpicedTwilight.MODID))
            .icon(() -> ModItems.FIRE_BEETLE_POWDER.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.acceptAll(Stream.of(
                        ModItems.FIRE_BEETLE_POWDER,
                        ModItems.HYDRA_HIDE,
                        ModItems.CINDER_POUCH,
                        ModItems.MAZE_SLIME_BEETLE_SPAWN_EGG
                ).map(sup -> {
                    return sup.get().getDefaultInstance();
                }).toList());
            }).build());
}
