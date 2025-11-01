package com.github.qichensn.event;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.data.LostMaidData;
import com.github.qichensn.data.TamedLostMaidCountAttachment;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDeathEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskIdle;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import static com.github.qichensn.data.LostMaidData.NOT_DROP_FILM;
import static com.github.qichensn.data.ModDataAttachment.TAMED_COUNT;
import static com.github.qichensn.util.ItemUtil.deleteBannedItems;


@EventBusSubscriber(modid = TouhouLostMaid.MODID)
public class ModMaidDeathEvent {
    @SubscribeEvent
    public static void onDeath(MaidDeathEvent  event) {
        EntityMaid maid = event.getMaid();
        DamageSource source = event.getSource();
        // 移除违禁品
        deleteBannedItems(maid);

        // 玩家击杀后驯服
        if(maid.getOrCreateData(LostMaidData.IS_LOST_MAID,false) && source.getEntity() instanceof Player player) {
            // 禁用FakePlayer
            if(player.isFakePlayer()){
                return;
            }
            maid.setOwnerUUID(player.getUUID());
            maid.setData(LostMaidData.IS_LOST_MAID,false);
            // 设置task
            TaskManager.findTask(TaskIdle.UID).ifPresent(maid::setTask);
            // 检查是否超过玩家可收服上限，如果超过，设置NOT_DROP_FILM为true，并且阻止生成胶片
            TamedLostMaidCountAttachment tamedCount = player.getData(TAMED_COUNT);
            TouhouLostMaid.LOGGER.info("测试 {}",tamedCount);
            if(tamedCount.canAdd()){
               tamedCount.add();
            }else {
                maid.setData(NOT_DROP_FILM,true);
                // 在生成坟墓的事件中处理
            }
        }
    }
}
