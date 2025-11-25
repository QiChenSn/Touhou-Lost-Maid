package com.github.qichensn.config;

import com.github.qichensn.TouhouLostMaid;
import com.github.qichensn.util.ItemUtil;
import com.google.common.collect.Lists;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static final ModConfigSpec SPEC = BUILDER.build();

    // 出生点保护半径
    public static ModConfigSpec.IntValue SPAWN_PROTECT_RANGE;

    // 不同类型迷失女仆生成权重
    public static ModConfigSpec.IntValue ATTACK_LOST_MAID_SPAWN_WEIGHT;
    public static ModConfigSpec.IntValue BOW_ATTACK_LOST_MAID_SPAWN_WEIGHT;
    public static ModConfigSpec.IntValue GUN_ATTACK_LOST_MAID_SPAWN_WEIGHT;

    // 女仆任务相关配置
    public static ModConfigSpec.DoubleValue TIPPED_ARROW_CHANCE;
    public static ModConfigSpec.DoubleValue BENEFIT_ARROW_CHANCE;

    // 女仆掉落物黑名单配置
    public static ModConfigSpec.ConfigValue<List<String>> BANNED_ITEMS;

    // 女仆装备黑名单配置
    public static ModConfigSpec.ConfigValue<List<String>> EQUIPMENT_BANNED_ITEMS;

    // 可驯服迷失女仆数量
    public static ModConfigSpec.IntValue CAN_TAME_COUNT;

    // 女仆模型白名单
    public static ModConfigSpec.ConfigValue<List<String>> MODEL_WHITE_LIST;

    public static ModConfigSpec init(){
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("女仆服务器配置");

        builder.comment("出生点附近禁止生成女仆的半径");
        SPAWN_PROTECT_RANGE = builder.defineInRange("spawn_protect_range", 1000, 0, Integer.MAX_VALUE);

        builder.comment("近战女仆生成权重");
        ATTACK_LOST_MAID_SPAWN_WEIGHT=builder.defineInRange("attack_lost_maid_spawn_weight",100,1,Integer.MAX_VALUE);

        builder.comment("弓箭远程女仆生成权重");
        BOW_ATTACK_LOST_MAID_SPAWN_WEIGHT=builder.defineInRange("bow_attack_lost_maid_spawn_weight",30,0,
                Integer.MAX_VALUE);

        builder.comment("枪械女仆生成权重");
        GUN_ATTACK_LOST_MAID_SPAWN_WEIGHT=builder.defineInRange("gun_attack_lost_maid_spawn_weight",10,0,
                Integer.MAX_VALUE);

        builder.comment("弓箭远程女仆使用药水箭的概率");
        TIPPED_ARROW_CHANCE=builder.defineInRange("tipped_arrow_chance",0.3,0,1);

        builder.comment("女仆射出的药水箭矢为正面效果的概率");
        builder.comment("说明: 正面效果可对玩家(刻意为之)和女仆生效, 负面效果只对玩家生效");
        BENEFIT_ARROW_CHANCE=builder.defineInRange("benefit_arrow_chance",0.2,0,1);

        builder.comment("女仆掉落物黑名单配置，支持正则表达式");
        builder.comment("说明: 填入物品的注册名，例如: [\"minecraft:diamond\", \"minecraft:enchanted_golden_apple\", \"^minecraft:.+$\"]");
        BANNED_ITEMS = builder.define("banned_items", Lists.newArrayList());

        builder.comment("女仆装备黑名单配置，支持正则表达式");
        EQUIPMENT_BANNED_ITEMS = builder.define("equipment_banned_items", Lists.newArrayList());

        builder.comment("可以通过驯服的迷失女仆的最大数量");
        CAN_TAME_COUNT = builder.defineInRange("can_tame_count",5,0,Integer.MAX_VALUE);

        builder.comment("女仆模型白名单（存在bug，暂时禁用）");
        builder.comment("说明: 填入模型名，例如: [\"geckolib:winefox\", \"geckolib:winefox_blue\"]");
        MODEL_WHITE_LIST = builder.define("model_white_list", Lists.newArrayList());

        return builder.build();
    }
}
