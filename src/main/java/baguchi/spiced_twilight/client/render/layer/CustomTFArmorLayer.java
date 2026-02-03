package baguchi.spiced_twilight.client.render.layer;

import baguchi.spiced_twilight.api.ISpicedArmor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.client.ClientHooks;
import org.joml.Quaternionf;

// Port from My mod. Bagus Lib
public class CustomTFArmorLayer<T extends LivingEntity, M extends EntityModel<T> & ISpicedArmor<T>> extends RenderLayer<T, M> {
    private final HumanoidModel defaultBipedModel;
    private final HumanoidModel innerModel;
    private final RenderLayerParent<T, M> renderer;
    private final TextureAtlas armorTrimAtlas;

    public CustomTFArmorLayer(RenderLayerParent<T, M> render, EntityRendererProvider.Context context) {
        super(render);
        this.defaultBipedModel = new HumanoidModel(context.bakeLayer(ModelLayers.ARMOR_STAND_OUTER_ARMOR));
        this.innerModel = new HumanoidModel(context.bakeLayer(ModelLayers.ARMOR_STAND_INNER_ARMOR));
        this.renderer = render;
        this.armorTrimAtlas = context.getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    public CustomTFArmorLayer(RenderLayerParent<T, M> render, EntityModelSet modelSet, ModelManager modelManager) {
        super(render);
        this.defaultBipedModel = new HumanoidModel(modelSet.bakeLayer(ModelLayers.ARMOR_STAND_OUTER_ARMOR));
        this.innerModel = new HumanoidModel(modelSet.bakeLayer(ModelLayers.ARMOR_STAND_INNER_ARMOR));
        this.renderer = render;
        this.armorTrimAtlas = modelManager.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    public static ResourceLocation getArmorResource(Entity entity, ItemStack stack, ArmorMaterial.Layer material, EquipmentSlot slot, boolean usesInner) {
        ArmorItem item = (ArmorItem) stack.getItem();
        String texture = item.getMaterial().getRegisteredName();
        String domain = "minecraft";
        int idx = texture.indexOf(58);
        if (idx != -1) {
            texture.substring(0, idx);
            texture.substring(idx + 1);
        }

        ResourceLocation s1 = ClientHooks.getArmorTexture(entity, stack, material, usesInner, slot);
        return s1;
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.pushPose();
        ItemStack headItem = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (headItem.getItem() instanceof ArmorItem armoritem) {
            if (headItem.canEquip(EquipmentSlot.HEAD, entity)) {
                for (ArmorMaterial.Layer armormaterial$layer : armoritem.getMaterial().value().layers()) {
                    boolean flag = usesInnerModel(armoritem.getEquipmentSlot());
                    HumanoidModel a = this.defaultBipedModel;
                    a = this.getArmorModelHook(entity, headItem, EquipmentSlot.HEAD, a);
                    boolean notAVanillaModel = a != this.defaultBipedModel;
                    this.setModelSlotVisible(a, EquipmentSlot.HEAD);
                    boolean flag1 = headItem.hasFoil();
                    if (headItem.is(ItemTags.DYEABLE)) {
                        int i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(headItem, -6265536));
                        this.renderHelmet(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, i, getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.HEAD, flag), notAVanillaModel);
                        this.renderHelmet(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, i, getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.HEAD, flag), notAVanillaModel);
                    } else {
                        this.renderHelmet(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F), getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.HEAD, flag), notAVanillaModel);
                    }
                }
            }
        }

        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        headItem = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (headItem.getItem() instanceof ArmorItem armoritem) {
            if (armoritem.getEquipmentSlot() == EquipmentSlot.CHEST) {
                for (ArmorMaterial.Layer armormaterial$layer : armoritem.getMaterial().value().layers()) {
                    boolean flag = usesInnerModel(armoritem.getEquipmentSlot());
                    HumanoidModel a = this.defaultBipedModel;
                    a = this.getArmorModelHook(entity, headItem, EquipmentSlot.CHEST, a);
                    boolean notAVanillaModel = a != this.defaultBipedModel;
                    this.setModelSlotVisible(a, EquipmentSlot.CHEST);
                    boolean flag1 = headItem.hasFoil();
                    if (headItem.is(ItemTags.DYEABLE)) {
                        int i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(headItem, -6265536));
                        float f = (float) (i >> 16 & 255) / 255.0F;
                        float f1 = (float) (i >> 8 & 255) / 255.0F;
                        float f2 = (float) (i & 255) / 255.0F;
                        this.renderChestplate(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, i, getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.CHEST, flag), notAVanillaModel);
                        this.renderChestplate(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, i, getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.CHEST, flag), notAVanillaModel);
                    } else {
                        this.renderChestplate(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F), getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.CHEST, flag), notAVanillaModel);
                    }
                }
            }
        }

        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        headItem = entity.getItemBySlot(EquipmentSlot.LEGS);
        if (headItem.getItem() instanceof ArmorItem armoritem) {
            if (armoritem.getEquipmentSlot() == EquipmentSlot.LEGS) {
                for (ArmorMaterial.Layer armormaterial$layer : armoritem.getMaterial().value().layers()) {
                    boolean flag = usesInnerModel(armoritem.getEquipmentSlot());
                    HumanoidModel a = this.innerModel;
                    a = this.getArmorModelHook(entity, headItem, EquipmentSlot.LEGS, a);
                    boolean notAVanillaModel = a != this.defaultBipedModel;
                    this.setModelSlotVisible(a, EquipmentSlot.LEGS);
                    boolean flag1 = headItem.hasFoil();
                    if (headItem.is(ItemTags.DYEABLE)) {
                        int i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(headItem, -6265536));
                        float f = (float) (i >> 16 & 255) / 255.0F;
                        float f1 = (float) (i >> 8 & 255) / 255.0F;
                        float f2 = (float) (i & 255) / 255.0F;
                        this.renderLeg(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, i, getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.LEGS, flag), notAVanillaModel);
                        this.renderLeg(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, i, getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.LEGS, flag), notAVanillaModel);
                    } else {
                        this.renderLeg(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F), getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.LEGS, flag), notAVanillaModel);
                    }
                }
            }
        }

        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        headItem = entity.getItemBySlot(EquipmentSlot.FEET);
        if (headItem.getItem() instanceof ArmorItem armoritem) {
            if (armoritem.getEquipmentSlot() == EquipmentSlot.FEET) {
                for (ArmorMaterial.Layer armormaterial$layer : armoritem.getMaterial().value().layers()) {
                    boolean flag = usesInnerModel(armoritem.getEquipmentSlot());
                    HumanoidModel a = this.defaultBipedModel;
                    a = this.getArmorModelHook(entity, headItem, EquipmentSlot.FEET, a);
                    boolean notAVanillaModel = a != this.defaultBipedModel;
                    this.setModelSlotVisible(a, EquipmentSlot.FEET);
                    boolean flag1 = headItem.hasFoil();
                    if (headItem.is(ItemTags.DYEABLE)) {
                        int i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(headItem, -6265536));
                        float f = (float) (i >> 16 & 255) / 255.0F;
                        float f1 = (float) (i >> 8 & 255) / 255.0F;
                        float f2 = (float) (i & 255) / 255.0F;
                        this.renderBoot(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, i, getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.FEET, flag), notAVanillaModel);
                        this.renderBoot(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, i, getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.FEET, flag), notAVanillaModel);
                    } else {
                        this.renderBoot(headItem, entity, matrixStackIn, bufferIn, packedLightIn, flag1, a, FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F), getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.FEET, flag), notAVanillaModel);
                    }
                }
            }
        }

        matrixStackIn.popPose();
    }

    private static boolean usesInnerModel(EquipmentSlot p_117129_) {
        return p_117129_ == EquipmentSlot.LEGS;
    }

    private HumanoidModel getArmorModel(EquipmentSlot p_117079_) {
        return usesInnerModel(p_117079_) ? this.innerModel : this.defaultBipedModel;
    }

    private void renderTrim(ModelPart part, Holder<ArmorMaterial> p_267946_, PoseStack p_268019_, MultiBufferSource p_268023_, int p_268190_, ArmorTrim p_267984_, boolean p_267965_, HumanoidModel p_267949_, boolean p_268259_, int color) {
        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(p_268259_ ? p_267984_.innerTexture(p_267946_) : p_267984_.outerTexture(p_267946_));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(ItemRenderer.getFoilBufferDirect(p_268023_, Sheets.armorTrimsSheet(p_267984_.pattern().value().decal()), true, p_267965_));
        part.render(p_268019_, vertexconsumer, p_268190_, OverlayTexture.NO_OVERLAY, color);
    }

    private void renderTrim(ModelPart part, ItemStack item, T entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, EquipmentSlot equipmentSlot, HumanoidModel modelIn) {
        Item var11 = item.getItem();
        if (var11 instanceof ArmorItem armorItem) {
            ArmorTrim armortrim = item.get(DataComponents.TRIM);
            if (armortrim != null) {
                this.renderTrim(part, armorItem.getMaterial(), matrixStackIn, bufferIn, packedLightIn, armortrim, glintIn, modelIn, usesInnerModel(equipmentSlot), FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F));
            }
        }

    }

    private void renderLeg(ItemStack legItem, T entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color, ResourceLocation armorResource, boolean notAVanillaModel) {
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(armorResource), false, glintIn);
        this.renderer.getModel().copyPropertiesTo(modelIn);
        modelIn.body.xRot = 0.0F;
        modelIn.body.yRot = 0.0F;
        modelIn.body.zRot = 0.0F;
        modelIn.body.x = 0.0F;
        modelIn.body.y = 0.0F;
        modelIn.body.z = 0.0F;
        modelIn.rightLeg.x = 0.0F;
        modelIn.rightLeg.xRot = 0.0F;
        modelIn.rightLeg.yRot = 0.0F;
        modelIn.rightLeg.zRot = 0.0F;
        modelIn.leftLeg.x = 0.0F;
        modelIn.leftLeg.xRot = 0.0F;
        modelIn.leftLeg.yRot = 0.0F;
        modelIn.leftLeg.zRot = 0.0F;
        modelIn.leftLeg.y = 0.0F;
        modelIn.rightLeg.y = 0.0F;
        modelIn.leftLeg.z = 0.0F;
        modelIn.rightLeg.z = 0.0F;
        this.renderer.getModel().rightLegPartArmors().forEach((part) -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToLeg(entity, part, matrixStackIn);
            modelIn.rightLeg.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            this.renderTrim(modelIn.rightLeg, legItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.LEGS, modelIn);
            matrixStackIn.popPose();
        });
        this.renderer.getModel().leftLegPartArmors().forEach((part) -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToLeg(entity, part, matrixStackIn);
            modelIn.leftLeg.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            this.renderTrim(modelIn.leftLeg, legItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.LEGS, modelIn);
            matrixStackIn.popPose();
        });
        this.renderer.getModel().bodyPartArmors().forEach((part) -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToChest(entity, part, matrixStackIn);
            modelIn.body.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            this.renderTrim(modelIn.body, legItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.LEGS, modelIn);
            matrixStackIn.popPose();
        });
    }

    private void renderBoot(ItemStack feetItem, T entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color, ResourceLocation armorResource, boolean notAVanillaModel) {
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(armorResource), false, glintIn);
        this.renderer.getModel().copyPropertiesTo(modelIn);
        modelIn.rightLeg.x = 0.0F;
        modelIn.rightLeg.xRot = 0.0F;
        modelIn.rightLeg.yRot = 0.0F;
        modelIn.rightLeg.zRot = 0.0F;
        modelIn.leftLeg.x = 0.0F;
        modelIn.leftLeg.xRot = 0.0F;
        modelIn.leftLeg.yRot = 0.0F;
        modelIn.leftLeg.zRot = 0.0F;
        modelIn.leftLeg.y = 0.0F;
        modelIn.rightLeg.y = 0.0F;
        modelIn.leftLeg.z = 0.0F;
        modelIn.rightLeg.z = 0.0F;
        this.renderer.getModel().rightLegPartArmors().forEach((part) -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToLeg(entity, part, matrixStackIn);
            modelIn.rightLeg.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            this.renderTrim(modelIn.rightLeg, feetItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.FEET, modelIn);
            matrixStackIn.popPose();
        });
        this.renderer.getModel().leftLegPartArmors().forEach((part) -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToLeg(entity, part, matrixStackIn);
            modelIn.leftLeg.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            this.renderTrim(modelIn.leftLeg, feetItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.FEET, modelIn);
            matrixStackIn.popPose();
        });
    }

    private void renderChestplate(ItemStack chestItem, T entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color, ResourceLocation armorResource, boolean notAVanillaModel) {
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(armorResource), false, glintIn);
        this.renderer.getModel().copyPropertiesTo(modelIn);
        modelIn.body.xRot = 0.0F;
        modelIn.body.yRot = 0.0F;
        modelIn.body.zRot = 0.0F;
        modelIn.body.x = 0.0F;
        modelIn.body.y = 0.0F;
        modelIn.body.z = 0.0F;
        modelIn.rightArm.x = 0.0F;
        modelIn.rightArm.y = 0.0F;
        modelIn.rightArm.z = 0.0F;
        modelIn.rightArm.xRot = 0.0F;
        modelIn.rightArm.yRot = 0.0F;
        modelIn.rightArm.zRot = 0.0F;
        modelIn.leftArm.x = 0.0F;
        modelIn.leftArm.y = 0.0F;
        modelIn.leftArm.z = 0.0F;
        modelIn.leftArm.xRot = 0.0F;
        modelIn.leftArm.yRot = 0.0F;
        modelIn.leftArm.zRot = 0.0F;
        modelIn.leftArm.y = 0.0F;
        modelIn.rightArm.y = 0.0F;
        modelIn.leftArm.z = 0.0F;
        modelIn.rightArm.z = 0.0F;
        this.renderer.getModel().rightHandArmors().forEach((part) -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToChestPat(entity, part, matrixStackIn);
            modelIn.rightArm.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            this.renderTrim(modelIn.rightArm, chestItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.CHEST, modelIn);
            matrixStackIn.popPose();
        });
        this.renderer.getModel().leftHandArmors().forEach((part) -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToChestPat(entity, part, matrixStackIn);
            modelIn.leftArm.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            this.renderTrim(modelIn.leftArm, chestItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.CHEST, modelIn);
            matrixStackIn.popPose();
        });
        this.renderer.getModel().bodyPartArmors().forEach((part) -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToChest(entity, part, matrixStackIn);
            modelIn.body.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            this.renderTrim(modelIn.body, chestItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.CHEST, modelIn);
            matrixStackIn.popPose();
        });
    }

    private void renderHelmet(ItemStack headItem, T entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color, ResourceLocation armorResource, boolean notAVanillaModel) {
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(armorResource), false, glintIn);
        this.renderer.getModel().copyPropertiesTo(modelIn);
        modelIn.head.xRot = 0.0F;
        modelIn.head.yRot = 0.0F;
        modelIn.head.zRot = 0.0F;
        modelIn.hat.xRot = 0.0F;
        modelIn.hat.yRot = 0.0F;
        modelIn.hat.zRot = 0.0F;
        modelIn.head.x = 0.0F;
        modelIn.head.y = 0.0F;
        modelIn.head.z = 0.0F;
        modelIn.hat.x = 0.0F;
        modelIn.hat.y = 0.0F;
        modelIn.hat.z = 0.0F;
        this.renderer.getModel().headPartArmors().forEach((part) -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToHead(entity, part, matrixStackIn);
            modelIn.head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            this.renderTrim(modelIn.head, headItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.HEAD, modelIn);
            matrixStackIn.popPose();
        });
    }

    protected void setModelSlotVisible(HumanoidModel p_188359_1_, EquipmentSlot slotIn) {
        this.setModelVisible(p_188359_1_);
        switch (slotIn) {
            case HEAD:
                p_188359_1_.head.visible = true;
                p_188359_1_.hat.visible = true;
                break;
            case CHEST:
                p_188359_1_.body.visible = true;
                p_188359_1_.rightArm.visible = true;
                p_188359_1_.leftArm.visible = true;
                break;
            case LEGS:
                p_188359_1_.body.visible = true;
                p_188359_1_.rightLeg.visible = true;
                p_188359_1_.leftLeg.visible = true;
                break;
            case FEET:
                p_188359_1_.rightLeg.visible = true;
                p_188359_1_.leftLeg.visible = true;
        }

    }

    protected void setModelVisible(HumanoidModel model) {
        model.setAllVisible(false);
    }

    protected HumanoidModel<?> getArmorModelHook(LivingEntity entity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel model) {
        Model basicModel = ClientHooks.getArmorModel(entity, itemStack, slot, model);
        return basicModel instanceof HumanoidModel ? (HumanoidModel) basicModel : model;
    }
}
