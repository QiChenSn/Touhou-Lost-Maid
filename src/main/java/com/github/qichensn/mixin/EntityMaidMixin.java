package com.github.qichensn.mixin;

import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityMaid.class)
public abstract class EntityMaidMixin extends TamableAnimal implements CrossbowAttackMob, IMaid {
    @Shadow
    public abstract <T> T getOrCreateData(TaskDataKey<T> dataKey, T defaultValue);

    protected EntityMaidMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tameMaid",at = @At("HEAD"), cancellable = true)
    public void tameMaid(ItemStack stack, Player player, CallbackInfoReturnable<InteractionResult> cir){
        Boolean isLost = this.getOrCreateData(LostMaidData.IS_LOST_MAID, false);
        if(isLost){
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
