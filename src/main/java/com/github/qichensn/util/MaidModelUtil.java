package com.github.qichensn.util;

import com.github.qichensn.network.ModMaidModelPackage;
import com.github.qichensn.network.ModSetMaidSoundIdPackage;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidModelPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetMaidSoundIdPackage;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MaidModelUtil {

    public static final List<CustomModelPack<MaidModelInfo>> packList;
    public static final List<MaidModelInfo> modelList;

    static {
        packList = CustomPackLoader.MAID_MODELS.getPackList();
        modelList = new ArrayList<>();
        for (CustomModelPack<MaidModelInfo> pack : packList) {
            modelList.addAll(pack.getModelList());
        }
    }

    /**
     * 切换女仆模型
     *
     * @param maid  女仆
     * @param info  模型信息
     */
    public static void notifyModelChange(EntityMaid maid, MaidModelInfo info) {
        if (info.getEasterEgg() == null) {
            PacketDistributor.sendToServer(new ModMaidModelPackage(maid.getId(), info.getModelId()));
            String useSoundPackId = info.getUseSoundPackId();
            if (StringUtils.isNotBlank(useSoundPackId)) {
                PacketDistributor.sendToServer(new ModSetMaidSoundIdPackage(maid.getId(), useSoundPackId));
            }
            // 切换模型时，重置手部动作
            maid.handItemsForAnimation[0] = ItemStack.EMPTY;
            maid.handItemsForAnimation[1] = ItemStack.EMPTY;
        }
    }


    /**
     * 通过模型ID切换女仆模型
     *
     * @param maid    女仆实体
     * @param modelId 模型ID
     */
    public static void notifyModelChange(EntityMaid maid, String modelId) {
        MaidModelInfo info = findModelInfoById(modelId);
        if (info != null) {
            notifyModelChange(maid, info);
        }
    }

    /**
     * 根据模型id查找女仆模型
     *
     * @param modelId 模型ID
     * @return 对应的女仆模型信息，如果未找到则返回null
     */
    public static MaidModelInfo findModelInfoById(String modelId) {
        if(StringUtils.isEmpty(modelId))return null;
        for (MaidModelInfo model : modelList) {
            if(model.getModelId().toString().toLowerCase(Locale.ENGLISH).equals(modelId.toLowerCase(Locale.ENGLISH))){
                return model;
            }
        }
        return null;
    }
}
