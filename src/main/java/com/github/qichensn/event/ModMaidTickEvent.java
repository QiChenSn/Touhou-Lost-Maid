package com.github.qichensn.event;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = TouhouLostMaid.MODID)
public class ModMaidTickEvent {
    @SubscribeEvent
    public static void handleLostMaid(MaidTickEvent event) {
        EntityMaid maid = event.getMaid();
        // 仅用于测试
        // TODO:删除测试逻辑
        if(maid.getMainHandItem().is(Items.DIAMOND_SWORD)){
            maid.setData(LostMaidData.IS_LOST_MAID, true);
        }else{
            maid.setData(LostMaidData.IS_LOST_MAID, false);
        }
    }
}
