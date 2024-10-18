package com.dxm.aimodel.modules.model.body;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Meng
 * Date: 2023/04/25
 * Desc:
 */
public class PromptBody {

    private List<ListItem> prompt;

    public PromptBody(List<ListItem> prompt) {
        this.prompt = prompt;
    }

    public PromptBody(ListItem item) {
        prompt = new ArrayList<>();
        prompt.add(item);
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
        return prompt;
    }

    public void setMessage(List<ListItem> message) {
        this.prompt = message;
    }

    @Override
    public String toString() {
        return "PromptBody{" +
                ", messages=" + prompt +
                '}';
    }
}
