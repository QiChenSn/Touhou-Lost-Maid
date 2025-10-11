package com.github.qichensn.event;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDeathEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = TouhouLostMaid.MODID)
public class ModMaidDeathEvent {
    @SubscribeEvent
    public static void onDeath(MaidDeathEvent  event) {
        EntityMaid maid = event.getMaid();
        DamageSource source = event.getSource();

        // 玩家击杀后驯服
        if(maid.getOrCreateData(LostMaidData.IS_LOST_MAID,false) && source.getEntity() instanceof Player player) {
            maid.setOwnerUUID(player.getUUID());
            maid.setData(LostMaidData.IS_LOST_MAID,true);
        }
    }
}
