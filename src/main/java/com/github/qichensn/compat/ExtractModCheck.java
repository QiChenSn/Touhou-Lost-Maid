package com.github.qichensn.compat;

import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.SWarfareCompat;

public class ExtractModCheck {
    // 注意: 此方法只有在FML后调用才是准确的
    // 如果在FML阶段应该先尝试调用init(), 再进行判断
    public static boolean isSWarfareLoaded(){
        return SWarfareCompat.isInstalled();
    }
}
