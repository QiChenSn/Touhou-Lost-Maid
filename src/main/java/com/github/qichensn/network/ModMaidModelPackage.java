package com.github.qichensn.network;

import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidModelPackage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record ModMaidModelPackage(int id, ResourceLocation modelId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ModMaidModelPackage> TYPE =
            new CustomPacketPayload.Type<>(getResourceLocation("mod_maid_model"));
    public static final StreamCodec<ByteBuf, ModMaidModelPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ModMaidModelPackage::id,
            ResourceLocation.STREAM_CODEC,
            ModMaidModelPackage::modelId,
            ModMaidModelPackage::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ModMaidModelPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                Entity entity = sender.level().getEntity(message.id);
                if (entity instanceof EntityMaid maid && (maid.isOwnedBy(sender) || maid.getOrCreateData(LostMaidData.IS_LOST_MAID,false))) {
                    if (sender.isCreative() || MaidConfig.MAID_CHANGE_MODEL.get()) {
                        maid.setIsYsmModel(false);
                        maid.setModelId(message.modelId.toString());
                        InitTrigger.MAID_EVENT.get().trigger(sender, TriggerType.CHANGE_MAID_MODEL);
                    } else {
                        sender.sendSystemMessage(Component.translatable("message.touhou_little_maid.change_model.disabled"));
                    }
                }
            });
        }
    }
}
