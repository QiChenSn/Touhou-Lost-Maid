package com.github.qichensn.task;

import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@LittleMaidExtension
public class MaidTaskRegister implements ILittleMaid {
    @Override
    public void addMaidTask(TaskManager manager) {
        manager.add(new AttackPlayerTask());
        manager.add(new BowAttackPlayerTask());
        manager.add(new GunAttackPlayerTask());
    }

    public static boolean canAttack(EntityMaid maid, LivingEntity target) {
        return target instanceof Player && !((Player) target).getAbilities().instabuild && maid.getOrCreateData(LostMaidData.IS_LOST_MAID,false);
    }
}
