package baguchi.spiced_twilight.client;

import baguchi.spiced_twilight.SpicedTwilight;
import baguchi.spiced_twilight.client.render.MazeSlimeBeetleRenderer;
import baguchi.spiced_twilight.client.render.layer.CustomTFArmorLayer;
import baguchi.spiced_twilight.entity.ModEntities;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.SlimeBeetleModel;
import twilightforest.init.TFEntities;

@EventBusSubscriber(modid = SpicedTwilight.MODID, value = Dist.CLIENT)
public class ClientRegistrar {

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MAZE_SLIME_BALL.get(),r -> new ThrownItemRenderer<>(r, 1.0F, false));
        event.registerEntityRenderer(ModEntities.MAZE_SLIME_BEETLE.get(), m ->  new MazeSlimeBeetleRenderer<>(m, new SlimeBeetleModel<>(m.bakeLayer(TFModelLayers.SLIME_BEETLE)), m.bakeLayer(TFModelLayers.SLIME_BEETLE_TAIL), 0.6F));
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        if (event.getRenderer(TFEntities.KOBOLD.get()) instanceof LivingEntityRenderer r) {
            ((LivingEntityRenderer<?, ?>) r).addLayer(new CustomTFArmorLayer<>(r, event.getContext()));
        }
    }
}
