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
        lostMaidSpawnConfig.put(LostMaidType.NORMAL,0);
        lostMaidSpawnConfig.put(LostMaidType.ATTACK,0);
        lostMaidSpawnConfig.put(LostMaidType.BOW_ATTACK,0);
        // 先检测已加载Mod
        int gun_attack_weight= ExtractModCheck.isSWarfareLoaded()?10:0;
        lostMaidSpawnConfig.put(LostMaidType.GUN_ATTACK,gun_attack_weight);
        for (LostMaidType value : LostMaidType.values()) {
            totalWeight += lostMaidSpawnConfig.get(value);
        }
    }
}
