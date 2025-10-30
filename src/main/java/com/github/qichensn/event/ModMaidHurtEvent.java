package com.github.qichensn.event;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

import static com.github.qichensn.util.RandomEquipment.isBeneficialPotion;


/**
 * 事件监听器，用于处理女仆受伤时的逻辑
 * 主要功能：
 * 1. 取消迷失女仆之间负面药水箭伤害
 */
@EventBusSubscriber(modid = TouhouLostMaid.MODID)
public class ModMaidHurtEvent {
    /**
     * 处理射弹命中事件，主要用于阻止迷失女仆之间的负面药水箭伤害
     *
     * @param event 射弹命中事件
     */
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent event) {
        // 空值检查
        if (event == null) {
            return;
        }

        // 获取射弹的发射者
        Entity attacker = event.getProjectile().getOwner();
        // 获取命中结果
        HitResult ray = event.getRayTraceResult();

        // 检查攻击者是否为可驯服动物且命中结果为实体
        if (attacker instanceof TamableAnimal && ray instanceof EntityHitResult hitResult) {
            Entity victim = hitResult.getEntity();

            // 检查攻击者和受害者是否都是女仆
            if (attacker instanceof EntityMaid attacker_maid && victim instanceof EntityMaid victim_maid) {
                // 检查攻击者和受害者是否都是迷失女仆
                boolean isAttackerLostMaid = attacker_maid.getOrCreateData(LostMaidData.IS_LOST_MAID, false);
                boolean isVictimLostMaid = victim_maid.getOrCreateData(LostMaidData.IS_LOST_MAID, false);

                if (isAttackerLostMaid && isVictimLostMaid) {
                    // 检查是否为药水箭（例如：普通药水箭、光灵箭）
                    // AbstractArrow 包含了普通箭、光灵箭等
                    if (event.getProjectile() instanceof AbstractArrow arrow) {
                        // 获取箭上的药水效果
                        PotionContents potionContents =
                                arrow.getPickupItemStackOrigin().get(DataComponents.POTION_CONTENTS);
                        if (potionContents != null && !potionContents.equals(PotionContents.EMPTY)) {
                            // 检查药水效果是否为正面效果
                            Potion value = potionContents.potion().get().value();
                            if(!isBeneficialPotion(value)){
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
        }
    }


}
