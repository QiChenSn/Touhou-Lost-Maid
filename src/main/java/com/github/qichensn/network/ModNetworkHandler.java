package com.github.qichensn.network;

import com.github.tartaricacid.touhoulittlemaid.network.message.MaidModelPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetMaidSoundIdPackage;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetworkHandler {
    private static final String VERSION = "1.0.0";

    public static void registerPacket(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VERSION).optional();

        registrar.playToServer(ModMaidModelPackage.TYPE, ModMaidModelPackage.STREAM_CODEC, ModMaidModelPackage::handle);
        registrar.playToServer(ModSetMaidSoundIdPackage.TYPE, ModSetMaidSoundIdPackage.STREAM_CODEC,
                ModSetMaidSoundIdPackage::handle);
    }
}
