package com.github.qichensn.task;

import com.github.qichensn.TouhouLostMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@LittleMaidExtension
public class AttackPlayerTask implements ILittleMaid {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TouhouLostMaid.MODID,
            "attack_player");

    class AttacjTask implements IMaidTask {


        @Override
        public ResourceLocation getUid() {
            return UID;
        }

        @Override
        public ItemStack getIcon() {
            return Items.ZOMBIE_SPAWN_EGG.getDefaultInstance();
        }

        @Override
        public @Nullable SoundEvent getAmbientSound(EntityMaid entityMaid) {
            return null;
        }

        @Override
        public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid entityMaid) {
            return List.of();
        }
    }

    @Override
    public void addMaidTask(TaskManager manager) {
        manager.add(new AttacjTask());
    }
}
