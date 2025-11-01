package com.github.qichensn.event;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTombstoneEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemFilm;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.items.ItemStackHandler;

import static com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil.findStackSlot;

@EventBusSubscriber(modid = TouhouLostMaid.MODID)
public class ModMaidTombstoneEvent {
    @SubscribeEvent
    public static void checkCanSpawnFile(MaidTombstoneEvent event) {
        EntityMaid maid = event.getMaid();
        EntityTombstone tombstone = event.getTombstone();
        if(maid.getOrCreateData(LostMaidData.NOT_DROP_FILM,false)){
            ItemStackHandler items = tombstone.getItems();
            // 删除胶片

            // 调用女仆工具类，查找胶片位置
            int slot = findStackSlot(items, (stack) -> stack.getItem() instanceof ItemFilm);

            if(slot>=0){
                items.extractItem(slot, 1, false);
            }
        }
    }
}
