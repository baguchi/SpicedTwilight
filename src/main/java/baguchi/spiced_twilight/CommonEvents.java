package baguchi.spiced_twilight;

import baguchi.spiced_twilight.attachment.BabyAttachment;
import baguchi.spiced_twilight.attachment.HelmetCrabHideAttachment;
import baguchi.spiced_twilight.attachment.ModAttachments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.level.ModifyCustomSpawnersEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import twilightforest.entity.monster.HelmetCrab;

@EventBusSubscriber(modid = SpicedTwilight.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onTick(EntityTickEvent.Post event) {
        HelmetCrabHideAttachment helmetCrabHideAttachment = event.getEntity().getData(ModAttachments.HELMET_CRAB_HIDE.get());

        if(helmetCrabHideAttachment != null){
            helmetCrabHideAttachment.tick(event.getEntity());
        }
        BabyAttachment baby = event.getEntity().getData(ModAttachments.BABY.get());

        if (baby != null) {
            baby.tick(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof HelmetCrab helmetCrab) {
            if (helmetCrab.getData(ModAttachments.HELMET_CRAB_HIDE).isHide()) {
                event.setCanceled(true);
            }else {
                helmetCrab.getData(ModAttachments.HELMET_CRAB_HIDE).hide();
                helmetCrab.syncData(ModAttachments.HELMET_CRAB_HIDE);
            }
        }
    }
}
