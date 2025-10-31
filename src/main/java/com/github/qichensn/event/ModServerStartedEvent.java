package com.github.qichensn.event;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.util.RandomEquipment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@EventBusSubscriber(modid = TouhouLostMaid.MODID)
public class ModServerStartedEvent {
    @SubscribeEvent
    public static void init(ServerAboutToStartEvent event) {
        RandomEquipment.init();
    }
}
