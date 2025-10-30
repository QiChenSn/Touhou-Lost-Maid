package com.github.qichensn.util;

import com.atsuishio.superbwarfare.init.ModItems;
import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.SWarfareCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.qichensn.compat.ExtractModCheck.isSWarfareLoaded;

public class ItemUtil {

    // TODO:可配置
    public static Set<Item> BANNED_ITEMS = new HashSet<>();

    static {
        // 先检查是否安装了卓越前线
        // 此方法应该在游戏内调用, 此时isInstalled返回的结果是准确的
        if(isSWarfareLoaded()){
            BANNED_ITEMS.add(ModItems.CREATIVE_AMMO_BOX.get());
        }
    }

    // 检查并且删除女仆背包中的违禁物品
    public static void deleteBannedItems(EntityMaid maid) {
        // 只处理迷失女仆
        if (!maid.getOrCreateData(LostMaidData.IS_LOST_MAID, false)) {
            return;
        }
        CombinedInvWrapper inv = maid.getAvailableInv(true);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            // 检查当前物品是否为违禁物品
            if (isBannedItem(stack)) {
                // 从背包中移除该物品
                inv.extractItem(i, stack.getCount(), false);
            }
        }
    }

    // 检查物品是否为违禁物品
    private static boolean isBannedItem(ItemStack stack) {
        return !stack.isEmpty() && BANNED_ITEMS.contains(stack.getItem());
    }
}
