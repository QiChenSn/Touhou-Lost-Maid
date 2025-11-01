package com.github.qichensn.data;

import com.github.qichensn.TouhouLostMaid;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModDataAttachment {
    // 注册
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, TouhouLostMaid.MODID);
    public static final Supplier<AttachmentType<TamedLostMaidCountAttachment>> TAMED_COUNT =
            ATTACHMENT_TYPES.register("tamed_count",()->TamedLostMaidCountAttachment.TYPE);
}
