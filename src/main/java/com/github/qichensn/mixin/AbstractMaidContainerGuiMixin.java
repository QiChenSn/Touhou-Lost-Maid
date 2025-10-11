package com.github.qichensn.mixin;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.data.LostMaidData;
import com.github.qichensn.task.AttackPlayerTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.TaskButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.rmi.server.UID;
import java.util.List;

@Mixin(AbstractMaidContainerGui.class)
public abstract class AbstractMaidContainerGuiMixin<T extends AbstractMaidContainer> extends AbstractContainerScreen<T> {
    @Shadow
    @Final
    protected EntityMaid maid;

    public AbstractMaidContainerGuiMixin(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "drawPerTaskButton",at = @At("HEAD"), cancellable = true)
    private void drawPerTaskButton(List<IMaidTask> tasks, int count, int index, CallbackInfo ci){
        IMaidTask ttask = tasks.get(index);
        if(ttask instanceof AttackPlayerTask.AttackTask attackTask){
            ci.cancel();
        }
    }
}
