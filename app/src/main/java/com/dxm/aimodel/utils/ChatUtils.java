package com.dxm.aimodel.utils;

import com.dxm.aimodel.modules.model.entity.ChatEntity;

import java.util.List;

/**
 * Author: meng
 * Date: 2024-04-05
 * Desc:
 */
public class ChatUtils {

    public static List<ChatEntity.Choice> parseChatMsg(ChatEntity ce) {
        return ce.getChoices();
    }
}
