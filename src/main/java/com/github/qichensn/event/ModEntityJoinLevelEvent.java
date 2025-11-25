package com.github.qichensn.event;

import com.atsuishio.superbwarfare.init.ModItems;
import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.config.ServerConfig;
import com.github.qichensn.data.LostMaidData;
import com.github.qichensn.data.LostMaidType;
import com.github.qichensn.task.AttackPlayerTask;
import com.github.qichensn.task.BowAttackPlayerTask;
import com.github.qichensn.task.GunAttackPlayerTask;
import com.github.qichensn.util.MaidModelUtil;
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
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.Map;

import static com.github.qichensn.util.RandomEquipment.getRandomWeapon;
import static com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil.findStackSlot;

@EventBusSubscriber
public class ModEntityJoinLevelEvent {
    @SubscribeEvent
    public static void markLostMaid(EntityJoinLevelEvent event){
        if (event.getEntity() instanceof EntityMaid maid && !event.getLevel().isClientSide()) {
            // 不对背包非空的女仆进行任何操作
            // 防止重复刷新女仆
            // 判断背包和装备栏是否都为空
            boolean isEmpty = true;

            // 检查基础背包
            ItemStackHandler maidInv = maid.getMaidInv();
            for(int i = 0; i < maidInv.getSlots(); i++){
                if(!maidInv.getStackInSlot(i).isEmpty()){
                    isEmpty = false;
                    break;
                }
            }

            // 检查装备栏（主手、副手、头盔、胸甲、护腿、鞋子）
            if(isEmpty) {
                for(EquipmentSlot slot : EquipmentSlot.values()) {
                    if(!maid.getItemBySlot(slot).isEmpty()) {
                        isEmpty = false;
                        break;
                    }
                }
            }

            if(!isEmpty) return;
            // 检查生成类型：判断条件改为:
            // 1.没有主人
            // 2.非结构生成

            // TODO 注意: 此处可能与其他自然生成女仆的方式冲突

            if (maid.getOwnerUUID()==null && !maid.isStructureSpawn()) {
                // 设置工作时间
                maid.setSchedule(MaidSchedule.ALL);
                maid.setData(LostMaidData.IS_LOST_MAID, true);

                setMaidType(maid);
                // changeRandomModel(maid);
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
        ItemStack gun = RandomEquipment.getRandomGun();
        if (!gun.isEmpty()) {
            maid.setItemSlot(EquipmentSlot.MAINHAND, gun);
        }
        // 需要处理此物品 防止被玩家获取到
        maid.setItemSlot(EquipmentSlot.OFFHAND, ModItems.CREATIVE_AMMO_BOX.get().getDefaultInstance());
    }

    private static void setBowAttackMaid(EntityMaid maid) {
        maid.setData(LostMaidData.LOST_MAID_TYPE, LostMaidType.BOW_ATTACK);
        TaskManager.findTask(BowAttackPlayerTask.UID).ifPresent(maid::setTask);
        ItemStack bow = Items.BOW.getDefaultInstance();
        if (!bow.isEmpty()) {
            maid.setItemSlot(EquipmentSlot.MAINHAND, bow);
        }
        // 开局一只箭 后续全靠刷
        ItemStack arrow = new ItemStack(Items.ARROW, 1);
        if (!arrow.isEmpty()) {
            maid.setItemSlot(EquipmentSlot.OFFHAND, arrow);
        }
    }


    // 配置近战女仆
    private static void setAttackMaid(EntityMaid maid){
        maid.setData(LostMaidData.LOST_MAID_TYPE, LostMaidType.ATTACK);
        // 设置task
        TaskManager.findTask(AttackPlayerTask.UID).ifPresent(maid::setTask);
        // 添加武器到主手
        ItemStack weapon = getRandomWeapon();
        if (!weapon.isEmpty()) {
            maid.setItemSlot(EquipmentSlot.MAINHAND, weapon);
        }
        // 添加盾牌到副手
        maid.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        // 添加护甲
        Map<EquipmentSlot, ItemStack> randomArmorSet = RandomEquipment.getRandomArmorSet();
        ItemStack headArmor = randomArmorSet.get(EquipmentSlot.HEAD);
        if (headArmor != null && !headArmor.isEmpty()) {
            maid.setItemSlot(EquipmentSlot.HEAD, headArmor);
        }
        ItemStack chestArmor = randomArmorSet.get(EquipmentSlot.CHEST);
        if (chestArmor != null && !chestArmor.isEmpty()) {
            maid.setItemSlot(EquipmentSlot.CHEST, chestArmor);
        }
        ItemStack legsArmor = randomArmorSet.get(EquipmentSlot.LEGS);
        if (legsArmor != null && !legsArmor.isEmpty()) {
            maid.setItemSlot(EquipmentSlot.LEGS, legsArmor);
        }
        ItemStack feetArmor = randomArmorSet.get(EquipmentSlot.FEET);
        if (feetArmor != null && !feetArmor.isEmpty()) {
            maid.setItemSlot(EquipmentSlot.FEET, feetArmor);
        }
    }

    public static void changeRandomModel(EntityMaid maid){
        if(!maid.getOrCreateData(LostMaidData.IS_LOST_MAID,false))return; // 判断是否为迷失女仆

        List<String> modelList = ServerConfig.MODEL_WHITE_LIST.get();
        if (modelList.isEmpty()) return;

        String randomModelId = modelList.get(maid.getRandom().nextInt(modelList.size()));
        MaidModelUtil.notifyModelChange(maid, randomModelId);
        TouhouLostMaid.LOGGER.info("修改模型：Lost Maid {} change model to {}", maid.getId(), randomModelId);
    }
}
