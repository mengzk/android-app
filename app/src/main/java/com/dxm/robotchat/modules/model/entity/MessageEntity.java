package com.dxm.robotchat.modules.model.entity;

import java.util.List;

/**
 * Author: Meng
 * Date: 2024/04/02
 * Modify: 2024/04/02
 * Desc:
 */
public class MessageEntity {
    private String content;
    private String role;
    private String id;

    public MessageEntity(String msg) {
        this.role = "input";
        this.content = msg;
    }

    public MessageEntity(ChatEntity.Message msg) {
        this.content = msg.getContent();
        this.role = msg.getRole();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
