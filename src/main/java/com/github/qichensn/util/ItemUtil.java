package com.github.qichensn.util;

import com.atsuishio.superbwarfare.init.ModItems;
import com.github.qichensn.config.ServerConfig;
import com.github.qichensn.data.LostMaidData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.qichensn.compat.ExtractModCheck.isSWarfareLoaded;
import static com.github.qichensn.util.RegexUtils.isValidRegex;
import static com.github.qichensn.util.RegexUtils.matches;

public class ItemUtil {

    public static Set<Item> BANNED_ITEMS = new HashSet<>();
    public static List<String> BANNED_ITEMS_REGEX = new ArrayList<>();
    public static Set<Item> EQUIPMENT_BANNED_ITEMS = new HashSet<>();
    public static List<String> EQUIPMENT_BANNED_ITEMS_REGEX = new ArrayList<>();

    static {
        // 添加通过配置文件定义的黑名单物品
        List<String> configuredItems = ServerConfig.BANNED_ITEMS.get();
        for (String itemString : configuredItems) {
            // 先检查是不是正则表达式
            if(isValidRegex(itemString)){
                BANNED_ITEMS_REGEX.add(itemString);
            }
            else if (BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemString))) {
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
            if(isValidRegex(itemString)){
                EQUIPMENT_BANNED_ITEMS_REGEX.add(itemString);
            }
            else if (BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemString))) {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemString));
                EQUIPMENT_BANNED_ITEMS.add(item);
            }
        }
    }

    // 获取女仆所有物品栏
    public static IItemHandlerModifiable getMaidInv(EntityMaid maid){
        return new CombinedInvWrapper(
                maid.getArmorInvWrapper(),
                maid.getHandsInvWrapper(),
                maid.getMaidInv(),
                maid.getMaidBauble(),
                maid.getHideInv(),
                maid.getTaskInv()
        );
    }

    // 检查并且删除女仆背包以及装备栏中的违禁物品
    public static void deleteBannedItems(EntityMaid maid) {
        // 只处理迷失女仆
        if (!maid.getOrCreateData(LostMaidData.IS_LOST_MAID, false)) {
            return;
        }
        IItemHandlerModifiable inv = maid.getAllInv();
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
        if(!stack.isEmpty() && BANNED_ITEMS.contains(stack.getItem())){
            return true;
        }
        // 尝试匹配正则表达式
        for (String regex : BANNED_ITEMS_REGEX) {
            if(matches(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(), regex)){
                return true;
            }
        }
        return false;
    }

    // 检查物品是否为违禁持有物
    public static boolean isEquipmentBannedItem(ItemStack stack) {
        return !stack.isEmpty() && isEquipmentBannedItem(stack.getItem());
    }

    // 检查物品是否为违禁持有物
    public static boolean isEquipmentBannedItem(Item item) {
        if(EQUIPMENT_BANNED_ITEMS.contains(item)){
            return true;
        }
        // 尝试匹配正则表达式
        for (String regex : EQUIPMENT_BANNED_ITEMS_REGEX) {
            if(matches(BuiltInRegistries.ITEM.getKey(item).toString(), regex)){
                return true;
            }
        }
        return false;
    }
}
