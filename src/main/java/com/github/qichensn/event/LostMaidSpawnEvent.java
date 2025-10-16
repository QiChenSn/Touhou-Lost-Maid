package com.github.qichensn.event;

import com.github.qichensn.TouhouLostMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.ServerLevelAccessor;
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
                LostMaidSpawnEvent::canLostMaidSpawn,  // 使用自定义生成规则：白天和黑夜都可以生成
                RegisterSpawnPlacementsEvent.Operation.AND);
    }


 /**
 * 自定义迷失女仆生成规则，移除了时间限制（白天/黑夜都可以生成）
 * 只检查方块是否为固体
 */
private static boolean canLostMaidSpawn(EntityType<EntityMaid> entityMaidEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
    // 检查位置是否在世界边界内
    if (!serverLevelAccessor.getWorldBorder().isWithinBounds(blockPos)) {
        return false;
    }

    // 检查方块是否为固体（可以站立）
    return serverLevelAccessor.getBlockState(blockPos.below()).isFaceSturdy(serverLevelAccessor, blockPos.below(), net.minecraft.core.Direction.UP);
}
}
