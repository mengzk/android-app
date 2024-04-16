package com.dxm.robotchat.modules.model.body;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Meng
 * Date: 2023/04/25
 * Desc:
 */
public class Model4Body {
    public String model = "glm-4";
    private List<ListItem> messages;

    public Model4Body(List<ListItem> prompt) {
        this.messages = prompt;
    }

    public Model4Body(ListItem item) {
        messages = new ArrayList<>();
        messages.add(item);
    }

    public static class ListItem {
        private String role;
        private String content;

        public ListItem(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public List<ListItem> getMessage() {
        return messages;
    }

    public void setMessage(List<ListItem> message) {
        this.messages = message;
    }

    @Override
    public String toString() {
        return "PromptBody{" +
                ", messages=" + messages +
                '}';
    }
}
