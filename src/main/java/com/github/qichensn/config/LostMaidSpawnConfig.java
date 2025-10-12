package com.github.qichensn.config;

import com.github.qichensn.data.LostMaidType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LostMaidSpawnConfig {
    // TODO:可配置

    // 不同类型女仆生成权重
    public static Map<LostMaidType,Integer> lostMaidSpawnConfig = new HashMap<LostMaidType,Integer>();
    public static int totalWeight;
    static {
        lostMaidSpawnConfig.put(LostMaidType.NORMAL,0);
        lostMaidSpawnConfig.put(LostMaidType.Attack,10);
        lostMaidSpawnConfig.put(LostMaidType.Ranged_Attack,0);
        lostMaidSpawnConfig.put(LostMaidType.Gun_Attack,0);
        for (LostMaidType value : LostMaidType.values()) {
            totalWeight += lostMaidSpawnConfig.get(value);
        }
    }
}
