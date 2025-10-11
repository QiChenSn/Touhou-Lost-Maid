package com.github.qichensn.util; // 您的包名

import com.github.qichensn.TouhouLostMaid;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.github.qichensn.TouhouLostMaid.MODID;

@EventBusSubscriber(modid = MODID)
public class RandomEquipment {

    // 用于缓存所有被判定为武器的物品列表。
    private static List<Item> WEAPON_LIST = null;
    private static final Random RANDOM = new Random();

    /**
     * 获取游戏中所有被判定为武器的物品列表。
     */
    @SubscribeEvent
    public static void getAllWeapons(RegisterEvent event) {
        if(WEAPON_LIST!=null)return;
        TouhouLostMaid.LOGGER.info("正在构建武器缓存...");
        List<Item> weaponList = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            ItemAttributeModifiers modifiers = item.getDefaultInstance().getAttributeModifiers();
            for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
                // 根据攻击伤害选择
                if (entry.attribute().equals(Attributes.ATTACK_DAMAGE)) {
                    AttributeModifier modifier = entry.modifier();
                    if (modifier.amount() > 0) {
                        boolean appliesToWeaponSlot = entry.slot().test(EquipmentSlot.MAINHAND);
                        if (appliesToWeaponSlot) {
                            weaponList.add(item);
                        }
                    }
                }
            }

        }
        WEAPON_LIST = Collections.unmodifiableList(weaponList);
        TouhouLostMaid.LOGGER.info("武器缓存构建完毕，共找到 " + WEAPON_LIST.size() + " 种武器。"+weaponList);
    }

    /**
     * 从游戏中所有武器中随机获取一个。
     * @return 一个包含随机武器的 ItemStack。如果游戏内没有武器，则返回 ItemStack.EMPTY。
     */
    public static ItemStack getRandomWeapon() {
        List<Item> weapons = WEAPON_LIST;
        if (weapons.isEmpty()) {
            return ItemStack.EMPTY;
        }
        Item randomWeaponItem = weapons.get(RANDOM.nextInt(weapons.size()));
        return new ItemStack(randomWeaponItem);
    }
}