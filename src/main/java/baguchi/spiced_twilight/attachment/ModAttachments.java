package baguchi.spiced_twilight.attachment;

import baguchi.spiced_twilight.SpicedTwilight;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, SpicedTwilight.MODID);

    public static final Supplier<AttachmentType<HelmetCrabHideAttachment>> HELMET_CRAB_HIDE = ATTACHMENT_TYPES.register(
            "helmet_crab_hide", () -> AttachmentType.serializable(HelmetCrabHideAttachment::new).sync(new AttachmentSyncHandler<>() {
                @Override
                public void write(RegistryFriendlyByteBuf registryFriendlyByteBuf, HelmetCrabHideAttachment tofuLivingAttachment, boolean b) {
                    registryFriendlyByteBuf.writeBoolean(tofuLivingAttachment.isHide());
                }

                @Override
                public @Nullable HelmetCrabHideAttachment read(IAttachmentHolder iAttachmentHolder, RegistryFriendlyByteBuf registryFriendlyByteBuf, @Nullable HelmetCrabHideAttachment tofuLivingAttachment) {
                    HelmetCrabHideAttachment attachment = new HelmetCrabHideAttachment();
                    attachment.setHide(registryFriendlyByteBuf.readBoolean());
                    return attachment;
                }
            }).build());
}