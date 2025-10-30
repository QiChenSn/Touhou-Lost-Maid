package com.github.qichensn.util;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.config.ServerConfig;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.common.GunCommonUtil;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.SWarfareCompat;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.github.qichensn.config.ServerConfig.BENEFIT_ARROW_CHANCE;

public class RandomEquipment {

    // 用于缓存所有被判定为武器的物品列表。
    private static List<Item> WEAPON_LIST = null;
    private static final Random RANDOM = new Random();

    // 用于缓存护甲套装的材料和对应部位物品。
    private static Map<String, Map<EquipmentSlot, Item>> ARMOR_MATERIALS = null;
    private static List<String> COMPLETE_MATERIALS = null;


    // 缓存所有女仆可用枪械
    private static List<Item> GUN_LIST =null;

    // 缓存所有箭矢物品
    private static List<Item> ARROW_LIST = null;

    // 缓存所有可用的药水效果
    private static List<Holder.Reference<Potion>> POTION_LIST = null;

    // 缓存所有增益药水效果
    private static List<Holder.Reference<Potion>> BENEFICIAL_POTION_LIST = null;

    // 缓存所有非增益药水效果
    private static List<Holder.Reference<Potion>> NOT_BENEFICIAL_POTION_LIST = null;

    public static void init() {
        getAllWeapons();
        getAllArmors();
        getAllGuns();
        getAllArrows();
        getAllPotions();
    }

    private static void getAllArrows() {
        if (ARROW_LIST != null) return;
        TouhouLostMaid.LOGGER.info("正在构建箭矢缓存...");
        List<Item> arrowList = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            // 使用 instanceof 检查是否为箭矢类型
            if (item instanceof ArrowItem) {
                arrowList.add(item);
                TouhouLostMaid.LOGGER.info("已将{}加入箭矢缓存列表",
                        item.getName(item.getDefaultInstance()));
            }
        }
        ARROW_LIST = Collections.unmodifiableList(arrowList);
        TouhouLostMaid.LOGGER.info("箭矢缓存构建完毕，共找到 {} 种箭矢。", ARROW_LIST.size());
    }

    /**
     * 判断药水是否为增益药水（正面效果）
     * @param potion 药水类型
     * @return 如果是增益药水返回true，否则返回false
     */
    public static boolean isBeneficialPotion(Potion potion) {
        if (potion == null) {
            return false;
        }
        List<MobEffectInstance> effects = potion.getEffects();
        // 如果没有效果，不视为增益药水
        if (effects.isEmpty()) {
            return false;
        }
        // 检查是否所有效果都是增益效果
        for (MobEffectInstance effect : effects) {
            if (effect == null) {
                continue;
            }
            // 如果有任何一个负面效果，则不是增益药水
            if (!effect.getEffect().value().isBeneficial()) {
                return false;
            }
        }
        return true;
    }

    private static void getAllPotions() {
        if (POTION_LIST != null) return;
        TouhouLostMaid.LOGGER.info("正在构建药水效果缓存...");
        List<Holder.Reference<Potion>> potionList = new ArrayList<>();
        List<Holder.Reference<Potion>> beneficialPotionList = new ArrayList<>();
        List<Holder.Reference<Potion>> harmfulPotionList = new ArrayList<>();

        for (Holder.Reference<Potion> potionHolder : BuiltInRegistries.POTION.holders().toList()) {
            Potion potion = potionHolder.value();
            // 过滤掉空药水和水瓶
            if (potion != Potions.POISON && potion != Potions.WATER) {
                potionList.add(potionHolder);

                // 根据药水效果分类
                if (isBeneficialPotion(potion)) {
                    beneficialPotionList.add(potionHolder);
                    TouhouLostMaid.LOGGER.info("已将{}加入增益药水效果缓存列表",
                            potionHolder.getRegisteredName());
                } else {
                    harmfulPotionList.add(potionHolder);
                    TouhouLostMaid.LOGGER.info("已将{}加入负面药水效果缓存列表",
                            potionHolder.getRegisteredName());
                }
            }
        }
        POTION_LIST = Collections.unmodifiableList(potionList);
        BENEFICIAL_POTION_LIST = Collections.unmodifiableList(beneficialPotionList);
        NOT_BENEFICIAL_POTION_LIST = Collections.unmodifiableList(harmfulPotionList);

        TouhouLostMaid.LOGGER.info("药水效果缓存构建完毕，共找到 {} 种药水效果。", POTION_LIST.size());
        TouhouLostMaid.LOGGER.info("增益药水效果缓存构建完毕，共找到 {} 种增益药水效果。", BENEFICIAL_POTION_LIST.size());
        TouhouLostMaid.LOGGER.info("非增益药水效果缓存构建完毕，共找到 {} 种非增益药水效果。", NOT_BENEFICIAL_POTION_LIST.size());
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

    private static void getAllWeapons() {
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

    private static void getAllArmors() {
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

    public static Holder.Reference<Potion> getRandomPotion(){
        float f = RANDOM.nextFloat();
        if(f <= BENEFIT_ARROW_CHANCE.get()){
            return getRandomBeneficialPotion();
        }
        return getRandomNotBeneficialPotion();
    }

    /**
     * 从增益药水效果列表中随机获取一个药水效果。
     * @return 一个增益药水效果的Holder引用。如果没有增益药水效果，则返回默认的POISON药水。
     */
    public static Holder.Reference<Potion> getRandomBeneficialPotion(){
        // 从缓存的增益药水效果列表中随机选取
        List<Holder.Reference<Potion>> beneficialPotions = BENEFICIAL_POTION_LIST;
        if(beneficialPotions != null && !beneficialPotions.isEmpty()){
            return beneficialPotions.get(RANDOM.nextInt(beneficialPotions.size()));
        }
        // 如果没有找到增益药水，返回一个默认的增益药水
        // 这里返回一个常见的增益药水，如力量药水或速度药水
        for (Holder.Reference<Potion> potionHolder : BuiltInRegistries.POTION.holders().toList()) {
            Potion potion = potionHolder.value();
            if (potion.getEffects().stream()
                    .allMatch(effect -> effect != null && effect.getEffect().value().isBeneficial()) && !potion.getEffects().isEmpty()) {
                return potionHolder;
            }
        }
        return (Holder.Reference<Potion>) Potions.STRENGTH; // 默认返回力量药水
    }

    /**
     * 从非增益药水效果列表中随机获取一个药水效果。
     * @return 一个非增益药水效果的Holder引用。如果没有负面药水效果，则返回默认的POISON药水。
     */
    public static Holder.Reference<Potion> getRandomNotBeneficialPotion(){
        List<Holder.Reference<Potion>> harmfulPotions = NOT_BENEFICIAL_POTION_LIST;
        if(harmfulPotions != null && !harmfulPotions.isEmpty()){
            return harmfulPotions.get(RANDOM.nextInt(harmfulPotions.size()));
        }
        // 返回默认的POISON药水
        return (Holder.Reference<Potion>) Potions.POISON;
    }

    /**
     * 从游戏中所有箭矢中随机获取一个。(注意:MC原版中有三种箭 我们应该优先返回药水箭)
     * 并且对于药水箭进行特殊处理
     * @return 一个包含随机箭矢的 ItemStack。如果游戏内没有箭矢，则返回 ItemStack.EMPTY。
     */
    public static ItemStack getRandomArrow() {
        // 单独判断药水箭
        float f = RANDOM.nextFloat();
        // TODO: 可配置
        if (f <= ServerConfig.TIPPED_ARROW_CHANCE.get()) {
            ItemStack arrow = new ItemStack(Items.TIPPED_ARROW);
            arrow.set(DataComponents.POTION_CONTENTS,new PotionContents(getRandomPotion()));
            return arrow;
        }

        List<Item> arrows = ARROW_LIST;
        if (arrows.isEmpty()) {
            return ItemStack.EMPTY;
        }
        Item randomArrowItem = arrows.get(RANDOM.nextInt(arrows.size()));
        return new ItemStack(randomArrowItem);
    }
}