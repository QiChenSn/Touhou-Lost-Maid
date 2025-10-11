package com.github.qichensn.event;

import com.github.qichensn.TouhouLostMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = TouhouLostMaid.MODID)
public class LostMaidSpawnEvent {
    @SubscribeEvent
    public static void spawn(RegisterSpawnPlacementsEvent event) {
        event.register(InitEntities.MAID.get(),
                SpawnPlacementTypes.ON_GROUND,  // 修复：使用新枚举
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,  // SpawnPredicate<T>：使用 Mob 的规则（适用于 Monster 子类）
                RegisterSpawnPlacementsEvent.Operation.AND);
    }
}
