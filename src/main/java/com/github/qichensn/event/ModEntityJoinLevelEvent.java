package com.github.qichensn.event;

import com.atsuishio.superbwarfare.init.ModItems;
import com.github.qichensn.data.LostMaidData;
import com.github.qichensn.data.LostMaidType;
import com.github.qichensn.task.AttackPlayerTask;
import com.github.qichensn.task.BowAttackPlayerTask;
import com.github.qichensn.task.GunAttackPlayerTask;
import com.github.qichensn.util.RandomEquipment;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.Map;

import static com.github.qichensn.util.RandomEquipment.getRandomWeapon;

@EventBusSubscriber
public class ModEntityJoinLevelEvent {
    @SubscribeEvent
    public static void markLostMaid(EntityJoinLevelEvent event){
        if (event.getEntity() instanceof EntityMaid maid && !event.getLevel().isClientSide()) {

            // 检查生成类型：判断条件改为:
            // 1.没有主人
            // 2.非结构生成

            // TODO 注意: 此处可能与其他自然生成女仆的方式冲突

            if (maid.getOwnerUUID()==null && !maid.isStructureSpawn()) {
                // 设置工作时间
                maid.setSchedule(MaidSchedule.ALL);
                maid.setData(LostMaidData.IS_LOST_MAID, true);

                setMaidType(maid);

            } else {
                maid.setData(LostMaidData.IS_LOST_MAID, false);
            }
        }
    }

    private static void setMaidType(EntityMaid maid) {
        // 根据权重随机确定女仆类型
        int random = maid.getRandom().nextInt(com.github.qichensn.config.LostMaidSpawnConfig.totalWeight);
        int currentWeight = 0;

        for (LostMaidType type : com.github.qichensn.config.LostMaidSpawnConfig.lostMaidSpawnConfig.keySet()) {
            currentWeight += com.github.qichensn.config.LostMaidSpawnConfig.lostMaidSpawnConfig.get(type);
            if (random < currentWeight) {
                // 根据不同类型设置相应的属性
                switch (type) {
                    case ATTACK:
                        setAttackMaid(maid);
                        break;
                    case BOW_ATTACK:
                        setBowAttackMaid(maid);
                        break;
                    case GUN_ATTACK:
                        setGunAttackMaid(maid);
                        break;
                    case NORMAL:
                    default:
                        break;
                }
                break;
            }
        }
    }
    private static void setGunAttackMaid(EntityMaid maid) {
        maid.setData(LostMaidData.LOST_MAID_TYPE, LostMaidType.GUN_ATTACK);
        TaskManager.findTask(GunAttackPlayerTask.UID).ifPresent(maid::setTask);
        maid.setItemSlot(EquipmentSlot.MAINHAND, RandomEquipment.getRandomGun());
        // 需要处理此物品 防止被玩家获取到
        maid.setItemSlot(EquipmentSlot.OFFHAND, ModItems.CREATIVE_AMMO_BOX.get().getDefaultInstance());
    }

    private static void setBowAttackMaid(EntityMaid maid) {
        maid.setData(LostMaidData.LOST_MAID_TYPE, LostMaidType.BOW_ATTACK);
        TaskManager.findTask(BowAttackPlayerTask.UID).ifPresent(maid::setTask);
        maid.setItemSlot(EquipmentSlot.MAINHAND, Items.BOW.getDefaultInstance());
        // 开局一只箭 后续全靠刷
        maid.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.ARROW,1));
    }


    // 配置近战女仆
    private static void setAttackMaid(EntityMaid maid){
        maid.setData(LostMaidData.LOST_MAID_TYPE, LostMaidType.ATTACK);
        // 设置task
        TaskManager.findTask(AttackPlayerTask.UID).ifPresent(maid::setTask);
        // 添加武器到主手
        maid.setItemSlot(EquipmentSlot.MAINHAND, getRandomWeapon());
        // 添加盾牌到副手
        maid.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        // 添加护甲
        Map<EquipmentSlot, ItemStack> randomArmorSet = RandomEquipment.getRandomArmorSet();
        maid.setItemSlot(EquipmentSlot.HEAD, randomArmorSet.get(EquipmentSlot.HEAD));
        maid.setItemSlot(EquipmentSlot.CHEST, randomArmorSet.get(EquipmentSlot.CHEST));
        maid.setItemSlot(EquipmentSlot.LEGS, randomArmorSet.get(EquipmentSlot.LEGS));
        maid.setItemSlot(EquipmentSlot.FEET, randomArmorSet.get(EquipmentSlot.FEET));
    }
}
