package com.github.qichensn.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static final ModConfigSpec SPEC = BUILDER.build();

    // 不同类型迷失女仆生成权重
    public static ModConfigSpec.IntValue ATTACK_LOST_MAID_SPAWN_WEIGHT;
    public static ModConfigSpec.IntValue BOW_ATTACK_LOST_MAID_SPAWN_WEIGHT;
    public static ModConfigSpec.IntValue GUN_ATTACK_LOST_MAID_SPAWN_WEIGHT;

    // 女仆任务相关配置
    public static ModConfigSpec.DoubleValue TIPPED_ARROW_CHANCE;

    public static ModConfigSpec init(){
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("女仆服务器配置");

        builder.comment("近战女仆生成权重");
        ATTACK_LOST_MAID_SPAWN_WEIGHT=builder.defineInRange("AttackLostMaidSpawnWeight",100,1,Integer.MAX_VALUE);

        builder.comment("弓箭远程女仆生成权重");
        BOW_ATTACK_LOST_MAID_SPAWN_WEIGHT=builder.defineInRange("BowAttackLostMaidSpawnWeight",30,0,Integer.MAX_VALUE);

        builder.comment("枪械女仆生成权重");
        GUN_ATTACK_LOST_MAID_SPAWN_WEIGHT=builder.defineInRange("GunAttackLostMaidSpawnWeight",10,0,Integer.MAX_VALUE);

        builder.comment("弓箭远程女仆使用药水箭的概率");
        TIPPED_ARROW_CHANCE=builder.defineInRange("Tipped_Arrow_Chance",0.3,0,1);

        return builder.build();
    }
}
