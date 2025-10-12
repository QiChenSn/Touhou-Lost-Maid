package com.github.qichensn.task;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;

@LittleMaidExtension
public class MaidTaskRegister implements ILittleMaid {
    @Override
    public void addMaidTask(TaskManager manager) {
        manager.add(new AttackPlayerTask());
    }
}
