package com.github.qichensn.data;

import com.github.qichensn.config.ServerConfig;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.attachment.AttachmentType;

public class TamedLostMaidCountAttachment {
    public static final AttachmentType<TamedLostMaidCountAttachment> TYPE = AttachmentType.builder(() -> new TamedLostMaidCountAttachment(0)).serialize(RecordCodecBuilder.create(ins -> ins.group(Codec.INT.fieldOf("num").forGetter(o -> o.num)).apply(ins, TamedLostMaidCountAttachment::new))).build();
    private int num;

    public TamedLostMaidCountAttachment(int i) {
        num = i;
    }

    public boolean canAdd() {
        return this.num + 1 <= getMaxNum();
    }

    public void add() {
        this.add(1);
    }

    public void add(int num) {
        if (num + this.num <= getMaxNum()) {
            this.num += num;
        } else {
            this.num = getMaxNum();
        }
    }

    public int get() {
        return num;
    }

    public int getMaxNum() {
        return ServerConfig.CAN_TAME_COUNT.get();
    }

    public void set(int num) {
        this.num = Mth.clamp(num, 0, getMaxNum());
    }

    public void min(int num) {
        if (num <= this.num) {
            this.num -= num;
        } else {
            this.num = 0;
        }
    }
}
