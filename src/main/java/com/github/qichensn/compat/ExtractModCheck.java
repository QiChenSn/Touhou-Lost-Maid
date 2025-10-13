package com.github.qichensn.compat;

import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.SWarfareCompat;

public class ExtractModCheck {
    public static boolean isSWarfareLoaded(){
        return SWarfareCompat.isInstalled();
    }

}
