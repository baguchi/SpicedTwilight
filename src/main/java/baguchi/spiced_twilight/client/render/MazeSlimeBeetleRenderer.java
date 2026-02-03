package baguchi.spiced_twilight.client.render;

import baguchi.spiced_twilight.SpicedTwilight;
import baguchi.spiced_twilight.entity.MazeSlimeBeetle;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.client.renderer.entity.SlimeBeetleRenderer;

public class MazeSlimeBeetleRenderer<T extends MazeSlimeBeetle, M extends HierarchicalModel<T>> extends SlimeBeetleRenderer<T, M> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SpicedTwilight.MODID, "textures/entity/maze_slime_beetle.png");


    public MazeSlimeBeetleRenderer(EntityRendererProvider.Context context, M model, ModelPart innerRoot, float shadowSize) {
        super(context, model, innerRoot, shadowSize);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
