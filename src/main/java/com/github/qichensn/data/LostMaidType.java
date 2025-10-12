package com.github.qichensn.data;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/**
 * 功能：定义了女仆丢失时可能发生的攻击类型。
 * 实现了 StringRepresentable 接口，以便能够使用字符串名称进行序列化，
 * 适用于配置文件、NBT 或其他数据存储。
 */
public enum LostMaidType implements StringRepresentable {
    // 枚举常量：定义三种攻击类型，并为其指定序列化时使用的字符串名称
    NORMAL("normal"),
    Attack("attack"),
    BOW_Attack("bow_attack"),
    Gun_Attack("gun_attack");


    // 存储用于序列化的字符串名称
    private final String serializedName;

    /**
     * 构造函数：初始化枚举常量及其序列化名称。
     * @param serializedName 在数据文件中（如JSON/NBT）表示此枚举常量的字符串。
     */
    LostMaidType(String serializedName) {
        this.serializedName = serializedName;
    }

    /**
     * 【StringRepresentable 接口方法】返回用于序列化的字符串表示。
     * 当 Codec 编码时，会调用此方法获取要写入数据的字符串。
     * @return 用于序列化的字符串名称。
     */
    @Override
    public String getSerializedName() {
        return this.serializedName;
    }

    /**
     * 【Codec】Mojang/NeoForged 的编解码器实例。
     * 作用：用于将 LostMaidType 枚举对象编码（序列化）成字符串，或从字符串解码（反序列化）回枚举对象。
     * 原理：使用 StringRepresentable.fromEnum 方法，它通过 getSerializedName() 进行编解码。
     */
    public static final Codec<LostMaidType> CODEC = StringRepresentable.fromEnum(LostMaidType::values);
}