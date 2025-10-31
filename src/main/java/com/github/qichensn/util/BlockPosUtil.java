package com.github.qichensn.util;

import net.minecraft.core.BlockPos;

public class BlockPosUtil {
    /**
     * 计算两个BlockPos之间的直线距离（三维欧几里得距离）
     * @return 两点之间的距离（浮点型）
     */
    public static double getDistance(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            throw new IllegalArgumentException("BlockPos不能为null");
        }

        int dx = pos2.getX() - pos1.getX();
        int dy = pos2.getY() - pos1.getY();
        int dz = pos2.getZ() - pos1.getZ();

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
