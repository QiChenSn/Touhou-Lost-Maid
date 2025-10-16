package com.github.qichensn.event;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidHurtEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = TouhouLostMaid.MODID)
public class ModMaidHurtEvent {
    @SubscribeEvent
    public static void cancelMaidHurtByMaid(MaidHurtEvent event) {
        EntityMaid maid = event.getMaid();
        Entity source = event.getSource().getEntity();
        if(source instanceof EntityMaid sourceMaid) {
            // 检查伤害源女仆是不是迷失状态
            if(sourceMaid.getOrCreateData(LostMaidData.IS_LOST_MAID,false)){
                event.setCanceled(true);
            }
        }
    }
}
