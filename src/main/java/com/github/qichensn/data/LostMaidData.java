package com.github.qichensn.data;

import com.github.qichensn.TouhouLostMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

@LittleMaidExtension
public class LostMaidData implements ILittleMaid {
    public static TaskDataKey<Boolean> IS_LOST_MAID;
    public static TaskDataKey<LostMaidType> LOST_MAID_TYPE;
    public static TaskDataKey<Boolean> NOT_DROP_FILM;
    @Override
    public void registerTaskData(TaskDataRegister register) {
        IS_LOST_MAID = register.register(
                ResourceLocation.fromNamespaceAndPath(TouhouLostMaid.MODID, "is_lost_maid"),
                Codec.BOOL.fieldOf("value").codec()  // 将布尔值包装在字段中
        );
        LOST_MAID_TYPE=register.register(
                ResourceLocation.fromNamespaceAndPath(TouhouLostMaid.MODID, "lost_maid_type"),
                LostMaidType.CODEC.fieldOf("value").codec()// 依旧包装...
        );
        NOT_DROP_FILM = register.register(
                ResourceLocation.fromNamespaceAndPath(TouhouLostMaid.MODID, "not_drop_film"),
                Codec.BOOL.fieldOf("value").codec()  // 将布尔值包装在字段中
        );
    }
}
