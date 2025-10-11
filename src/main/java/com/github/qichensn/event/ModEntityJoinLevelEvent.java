package com.github.qichensn.event;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import static com.github.qichensn.task.AttackPlayerTask.UID;

@EventBusSubscriber
public class ModEntityJoinLevelEvent {
    @SubscribeEvent
    public static void markLostMaid(EntityJoinLevelEvent event){
        if (event.getEntity() instanceof EntityMaid maid && !event.getLevel().isClientSide()) {

            TouhouLostMaid.LOGGER.info("检测到女仆生成:"+maid+"\n生成方式:"+maid.getSpawnType());

            // 检查生成类型：仅自然生成时标记
            if (maid.getSpawnType() == MobSpawnType.NATURAL) {
                maid.setData(LostMaidData.IS_LOST_MAID, true);
                // 设置工作时间
                maid.setSchedule(MaidSchedule.ALL);
                // 设置task
                TaskManager.findTask(UID).ifPresent(maid::setTask);

                // 发放武器

                // 添加武器到主手
                maid.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                // 添加盾牌到副手
                maid.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
                // 添加护甲
                maid.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                maid.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                maid.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                maid.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));

            } else {
                maid.setData(LostMaidData.IS_LOST_MAID, false);
            }
        }
    }
}
