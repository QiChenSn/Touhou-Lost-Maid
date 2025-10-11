package com.github.qichensn.event;

import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import static com.github.qichensn.task.AttackPlayerTask.UID;
import static com.github.qichensn.util.RandomEquipment.getRandomWeapon;

@EventBusSubscriber
public class ModEntityJoinLevelEvent {
    @SubscribeEvent
    public static void markLostMaid(EntityJoinLevelEvent event){
        if (event.getEntity() instanceof EntityMaid maid && !event.getLevel().isClientSide()) {

            // 检查生成类型：判断条件改为:
            // 1.没有主人
            // 2.非结构生成
            if (maid.getOwnerUUID()==null && !maid.isStructureSpawn()) {
                maid.setData(LostMaidData.IS_LOST_MAID, true);
                // 设置工作时间
                maid.setSchedule(MaidSchedule.ALL);
                // 设置task
                TaskManager.findTask(UID).ifPresent(maid::setTask);

                // 给女仆穿装备
                equipMaid(maid);
            } else {
                maid.setData(LostMaidData.IS_LOST_MAID, false);
            }
        }
    }

    public static void equipMaid(EntityMaid maid){
        // 添加武器到主手
        maid.setItemSlot(EquipmentSlot.MAINHAND, getRandomWeapon());
        // 添加盾牌到副手
        maid.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        // 添加护甲
        maid.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
        maid.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
        maid.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
        maid.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
    }
}
