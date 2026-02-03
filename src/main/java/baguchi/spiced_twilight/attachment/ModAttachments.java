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
                public void write(RegistryFriendlyByteBuf registryFriendlyByteBuf, HelmetCrabHideAttachment attachment, boolean b) {
                    registryFriendlyByteBuf.writeBoolean(attachment.isHide());
                }

                @Override
                public @Nullable HelmetCrabHideAttachment read(IAttachmentHolder iAttachmentHolder, RegistryFriendlyByteBuf registryFriendlyByteBuf, @Nullable HelmetCrabHideAttachment attachment) {
                    HelmetCrabHideAttachment attachment2 = new HelmetCrabHideAttachment();
                    attachment2.setHide(registryFriendlyByteBuf.readBoolean());
                    return attachment2;
                }
            }).build());
    public static final Supplier<AttachmentType<BabyAttachment>> BABY = ATTACHMENT_TYPES.register(
            "baby", () -> AttachmentType.serializable(BabyAttachment::new).sync(new AttachmentSyncHandler<>() {
                @Override
                public void write(RegistryFriendlyByteBuf registryFriendlyByteBuf, BabyAttachment attachment, boolean b) {
                    registryFriendlyByteBuf.writeBoolean(attachment.isBaby());
                }

                @Override
                public @Nullable BabyAttachment read(IAttachmentHolder iAttachmentHolder, RegistryFriendlyByteBuf registryFriendlyByteBuf, @Nullable BabyAttachment attachment) {
                    BabyAttachment attachment2 = new BabyAttachment();
                    attachment2.setBaby(registryFriendlyByteBuf.readBoolean());
                    return attachment2;
                }
            }).build());
}