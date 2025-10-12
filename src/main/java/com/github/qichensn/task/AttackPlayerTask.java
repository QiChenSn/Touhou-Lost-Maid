package com.github.qichensn.task;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidUseShieldTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AttackPlayerTask implements IAttackTask {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TouhouLostMaid.MODID,
            "attack_player");

    // 女仆追击距离
    // TODO:可配置
    private static final float ATTACK_RADIUS=40.0f;

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.ZOMBIE_SPAWN_EGG.getDefaultInstance();
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        return maid.getOrCreateData(LostMaidData.IS_LOST_MAID,false);
    }

    @Override
    public @Nullable SoundEvent getAmbientSound(EntityMaid entityMaid) {
        return null;
    }

    @Override
    public boolean canAttack(EntityMaid maid, LivingEntity target) {
        return MaidTaskRegister.canAttack(maid,target);
    }

    @Override
    public boolean isWeapon(EntityMaid maid, ItemStack stack) {
        return stack.getAttributeModifiers().modifiers()
                .stream()
                .anyMatch(modifier -> modifier.attribute().is(Attributes.ATTACK_DAMAGE));
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid entityMaid) {

        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(this::hasAssaultWeapon,IAttackTask::findFirstValidAttackTarget );
        BehaviorControl<EntityMaid> findTargetTask =
                StopAttackingIfTargetInvalid.create(target -> !hasAssaultWeapon(entityMaid) || farAway(target, entityMaid));
        BehaviorControl<Mob> moveToTargetTask = SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(0.6f);
        BehaviorControl<Mob> attackTargetTask = MeleeAttack.create(20);
        MaidUseShieldTask maidUseShieldTask = new MaidUseShieldTask();

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, moveToTargetTask),
                Pair.of(5, attackTargetTask),
                Pair.of(5, maidUseShieldTask)
        );
    }

    private boolean hasAssaultWeapon(EntityMaid maid) {
        return isWeapon(maid, maid.getMainHandItem());
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        if (!target.isAlive()) {
            return true;
        }
        boolean enable = maid.isHomeModeEnable();
        float radius = ATTACK_RADIUS;
        if (!enable && maid.getOwner() != null) {
            return maid.getOwner().distanceTo(target) > radius;
        }
        return maid.distanceTo(target) > radius;
    }
}
