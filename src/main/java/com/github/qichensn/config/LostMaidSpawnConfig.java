package com.github.qichensn.config;

import com.github.qichensn.compat.ExtractModCheck;
import com.github.qichensn.data.LostMaidType;

import java.util.HashMap;
import java.util.Map;

public class LostMaidSpawnConfig {
    // TODO:可配置

    // 不同类型女仆生成权重
    public static Map<LostMaidType,Integer> lostMaidSpawnConfig = new HashMap<LostMaidType,Integer>();
    public static int totalWeight;
    static {
        lostMaidSpawnConfig.put(LostMaidType.NORMAL,0); // 仅仅为了兼容,实际上没有生成普通女仆的逻辑
        lostMaidSpawnConfig.put(LostMaidType.ATTACK,ServerConfig.ATTACK_LOST_MAID_SPAWN_WEIGHT.get());
        lostMaidSpawnConfig.put(LostMaidType.BOW_ATTACK,ServerConfig.BOW_ATTACK_LOST_MAID_SPAWN_WEIGHT.get());
        // 先检测已加载Mod
        int gun_attack_weight= ExtractModCheck.isSWarfareLoaded()?ServerConfig.GUN_ATTACK_LOST_MAID_SPAWN_WEIGHT.get():0;
        lostMaidSpawnConfig.put(LostMaidType.GUN_ATTACK,gun_attack_weight);
        for (LostMaidType value : LostMaidType.values()) {
            totalWeight += lostMaidSpawnConfig.get(value);
        }
    }
}
