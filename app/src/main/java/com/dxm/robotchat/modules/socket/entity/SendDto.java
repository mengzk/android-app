package com.dxm.robotchat.modules.socket.entity;

import com.dxm.robotchat.config.AppConfig;

/**
 * Author: Meng
 * Date: 2023/04/14
 * Desc:
 */
public class SendDto<T> {
    private String event;
    private int tag = 0;
    private String formId = AppConfig.device_id;
    private String toId;
    private int status = 0;
    private T data;

    public SendDto(String toId , T data) {
        this.toId = toId;
        this.data = data;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
