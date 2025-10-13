package com.github.qichensn.util; // 您的包名

import com.github.qichensn.TouhouLostMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.common.GunCommonUtil;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.SWarfareCompat;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.github.qichensn.TouhouLostMaid.MODID;

public class RandomEquipment {

    // 用于缓存所有被判定为武器的物品列表。
    private static List<Item> WEAPON_LIST = null;
    private static final Random RANDOM = new Random();

    // 用于缓存护甲套装的材料和对应部位物品。
    private static Map<String, Map<EquipmentSlot, Item>> ARMOR_MATERIALS = null;
    private static List<String> COMPLETE_MATERIALS = null;


    // 缓存所有女仆可用枪械
    private static List<Item> GUN_LIST =null;

    public static void init() {
        getAllWeapons();
        getAllArmors();
        getAllGuns();
    }

    private static void getAllGuns() {
        // 兼容女仆工具类加载
        // 已确保此方法被调用时一定加载了卓越前线mod
        // 并且此方法在FML加载阶段调用
        if(!SWarfareCompat.isInstalled()){
            SWarfareCompat.init();
        }
        if(GUN_LIST!=null) return;
        TouhouLostMaid.LOGGER.info("正在构建枪械缓存...");
        List<Item> gunList = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM){
            if(GunCommonUtil.isGun(item.getDefaultInstance())){
                gunList.add(item);
                TouhouLostMaid.LOGGER.info("已将{}加入枪械缓存列表",
                        item.getName(item.getDefaultInstance()));
            }
        }
        GUN_LIST = Collections.unmodifiableList(gunList);
        TouhouLostMaid.LOGGER.info("枪械缓存构建完毕，共找到 {} 种枪械。", GUN_LIST.size());
    }

    public static void getAllWeapons() {
        if(WEAPON_LIST!=null)return;
        TouhouLostMaid.LOGGER.info("正在构建武器缓存...");
        List<Item> weaponList = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            ItemAttributeModifiers modifiers = item.getDefaultInstance().getAttributeModifiers();
            for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
                // 根据攻击伤害选择
                // 要求攻击伤害大于2
                if (entry.attribute().equals(Attributes.ATTACK_DAMAGE)) {
                    AttributeModifier modifier = entry.modifier();
                    if (modifier.amount() > 2) {
                        boolean appliesToWeaponSlot = entry.slot().test(EquipmentSlot.MAINHAND);
                        if (appliesToWeaponSlot) {
                            weaponList.add(item);
                            TouhouLostMaid.LOGGER.info("已将{}加入武器缓存列表",
                                    item.getName(item.getDefaultInstance()));
                        }
                    }
                }
            }

        }
        WEAPON_LIST = Collections.unmodifiableList(weaponList);
        TouhouLostMaid.LOGGER.info("武器缓存构建完毕，共找到 {} 种武器。", WEAPON_LIST.size());
    }

    public static void getAllArmors() {
        if (ARMOR_MATERIALS != null) return;
        TouhouLostMaid.LOGGER.info("正在构建护甲套装缓存...");
        Map<String, Map<EquipmentSlot, Item>> armorMaterials = new HashMap<>();
        Map<String, EquipmentSlot> typeToSlot = new HashMap<>();
        typeToSlot.put("helmet", EquipmentSlot.HEAD);
        typeToSlot.put("chestplate", EquipmentSlot.CHEST);
        typeToSlot.put("leggings", EquipmentSlot.LEGS);
        typeToSlot.put("boots", EquipmentSlot.FEET);

        for (Item item : BuiltInRegistries.ITEM) {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
            if (key == null) continue;
            String path = key.getPath();
            String[] parts = path.split("_");
            if (parts.length < 2) continue;
            String material = parts[0];
            String type = parts[parts.length - 1];
            EquipmentSlot expectedSlot = typeToSlot.get(type);
            if (expectedSlot == null) continue;

            ItemAttributeModifiers modifiers = item.getDefaultInstance().getAttributeModifiers();
            boolean hasArmor = false;
            for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
                if (entry.attribute().equals(Attributes.ARMOR)) {
                    AttributeModifier modifier = entry.modifier();
                    if (modifier.amount() > 0 && entry.slot().test(expectedSlot)) {
                        hasArmor = true;
                        break;
                    }
                }
            }
            if (hasArmor) {
                armorMaterials.computeIfAbsent(material, k -> new HashMap<>()).put(expectedSlot, item);
                TouhouLostMaid.LOGGER.info("已将{}加入护甲套装缓存（材料：{}，部位：{}）",
                        item.getName(item.getDefaultInstance()), material, expectedSlot);
            }
        }

        List<String> completeMaterials = new ArrayList<>();
        for (Map.Entry<String, Map<EquipmentSlot, Item>> entry : armorMaterials.entrySet()) {
            if (entry.getValue().size() == 4) {
                completeMaterials.add(entry.getKey());
            }
        }

        ARMOR_MATERIALS = Collections.unmodifiableMap(armorMaterials);
        COMPLETE_MATERIALS = Collections.unmodifiableList(completeMaterials);
        TouhouLostMaid.LOGGER.info("护甲套装缓存构建完毕，共找到 {} 种完整套装。", COMPLETE_MATERIALS.size());
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

    /**
     * 从游戏中所有完整护甲套装中随机获取一套。
     * @return 一个映射，键为 EquipmentSlot（HEAD, CHEST, LEGS, FEET），值为对应部位的 ItemStack。
     *         如果游戏内没有完整护甲套装，则返回空 Map。
     */
    public static Map<EquipmentSlot, ItemStack> getRandomArmorSet() {
        List<String> completeMaterials = COMPLETE_MATERIALS;
        if (completeMaterials.isEmpty()) {
            return Collections.emptyMap();
        }
        String randomMaterial = completeMaterials.get(RANDOM.nextInt(completeMaterials.size()));
        Map<EquipmentSlot, Item> set = ARMOR_MATERIALS.get(randomMaterial);
        Map<EquipmentSlot, ItemStack> result = new HashMap<>();
        for (Map.Entry<EquipmentSlot, Item> entry : set.entrySet()) {
            result.put(entry.getKey(), new ItemStack(entry.getValue()));
        }
        return result;
    }

    // 获取随机可用枪械
    public static ItemStack getRandomGun() {
        List<Item> gunList = GUN_LIST;
        if (gunList.isEmpty()) {
            return ItemStack.EMPTY;
        }
        Item randomGunItem = gunList.get(RANDOM.nextInt(gunList.size()));
        return new ItemStack(randomGunItem);
    }
}