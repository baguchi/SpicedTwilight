package baguchi.spiced_twilight;

import baguchi.spiced_twilight.attachment.BabyAttachment;
import baguchi.spiced_twilight.attachment.HelmetCrabHideAttachment;
import baguchi.spiced_twilight.attachment.ModAttachments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
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
            HelmetCrabHideAttachment attachment = helmetCrab.getData(ModAttachments.HELMET_CRAB_HIDE);
            if (attachment.isHide()) {
                event.setCanceled(true);
            }else {
                float amount = event.getAmount();
                float hideChance = amount * 0.1F;

                if (attachment.getHideChance() + hideChance < helmetCrab.getRandom().nextFloat()) {
                    attachment.hide();
                    helmetCrab.syncData(ModAttachments.HELMET_CRAB_HIDE);
                } else {
                    attachment.setHideChance(attachment.getHideChance() + hideChance);
                }
            }
        }
    }
}
