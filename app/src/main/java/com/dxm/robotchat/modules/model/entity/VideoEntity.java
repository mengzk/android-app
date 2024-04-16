package com.dxm.robotchat.modules.model.entity;

import java.util.ArrayList;

/**
 * Author: Meng
 * Date: 2023/04/26
 * Desc:
 */
public class VideoEntity {
    public ArrayList<ItemBean> itemList;
    public static class ItemBean {
        public String type;
        public DataBean data;
        public String tag;
        public int id;
    }

    public static class DataBean {
        public int id;
        public String title;
        public String description;
        public String category;
        public String playUrl;
    }

    @Override
    public String toString() {
        return "VideoEntity{" +
                "itemList=" + itemList +
                '}';
    }
}