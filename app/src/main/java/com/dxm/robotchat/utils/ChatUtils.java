package com.dxm.robotchat.utils;

import com.dxm.robotchat.modules.model.entity.ChatEntity;

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
