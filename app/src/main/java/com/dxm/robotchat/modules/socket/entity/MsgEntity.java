package com.dxm.robotchat.modules.socket.entity;

/**
 * Author: Meng
 * Date: 2023/04/14
 * Desc:
 */
public class MsgEntity {
    private String event;
    private int tag;
    private String formId;
    private String toId;
    private int status;
    private String data;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MsgEntity{" +
                "tag=" + tag +
                ", formId='" + formId + '\'' +
                ", toId='" + toId + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }
}
