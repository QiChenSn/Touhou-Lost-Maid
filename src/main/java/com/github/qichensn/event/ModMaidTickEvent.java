package com.github.qichensn.event;

import com.github.qichensn.TouhouLostMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = TouhouLostMaid.MODID)
public class ModMaidTickEvent {
    @SubscribeEvent
    public static void handleLostMaid(MaidTickEvent event) {
        EntityMaid maid = event.getMaid();
    }
}
