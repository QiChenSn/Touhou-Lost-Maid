package com.github.qichensn.event;

import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber
public class ModEntityJoinLevelEvent {
    @SubscribeEvent
    public static void markLostMaid(EntityJoinLevelEvent event){
        if (event.getEntity() instanceof EntityMaid maid && !event.getLevel().isClientSide()) {
            // 检查生成类型：仅自然生成时标记
            if (maid.getSpawnType() == MobSpawnType.SPAWNER) {
                maid.setData(LostMaidData.IS_LOST_MAID, true);
            } else {
                maid.setData(LostMaidData.IS_LOST_MAID, false);
            }
        }
    }
}
