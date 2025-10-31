package com.github.qichensn.util;

import com.atsuishio.superbwarfare.init.ModItems;
import com.github.qichensn.config.ServerConfig;
import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.qichensn.compat.ExtractModCheck.isSWarfareLoaded;

public class ItemUtil {

    public static Set<Item> BANNED_ITEMS = new HashSet<>();
    public static Set<Item> EQUIPMENT_BANNED_ITEMS = new HashSet<>();

    static {
        // 添加通过配置文件定义的黑名单物品
        List<String> configuredItems = ServerConfig.BANNED_ITEMS.get();
        for (String itemString : configuredItems) {
            // 先检查一下，确保游戏中确实有该物品
            if (BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemString))) {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemString));
                BANNED_ITEMS.add(item);
            }
        }
        // 强制禁止掉落的物品
        if(isSWarfareLoaded()){
            BANNED_ITEMS.add(ModItems.CREATIVE_AMMO_BOX.get());
        }

        // 装备黑名单
        List<String> bannedItems = ServerConfig.EQUIPMENT_BANNED_ITEMS.get();
        for (String itemString : bannedItems) {
            if (BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemString))) {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemString));
                EQUIPMENT_BANNED_ITEMS.add(item);
            }
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

    // 检查物品是否为违禁掉落物
    public static boolean isBannedItem(ItemStack stack) {
        return !stack.isEmpty() && BANNED_ITEMS.contains(stack.getItem());
    }

    // 检查物品是否为违禁持有物
    public static boolean isEquipmentBannedItem(ItemStack stack) {
        return !stack.isEmpty() && EQUIPMENT_BANNED_ITEMS.contains(stack.getItem());
    }

    // 检查物品是否为违禁持有物
    public static boolean isEquipmentBannedItem(Item item) {
        return EQUIPMENT_BANNED_ITEMS.contains(item);
    }
}
