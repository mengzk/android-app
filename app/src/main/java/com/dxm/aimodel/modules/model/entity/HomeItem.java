package com.dxm.aimodel.modules.model.entity;

public class HomeItem {
    public String name;
    public String desc;
    public String icon;

    public int id;

    public HomeItem(String name, String desc, int id) {
        this(name, desc, "", id);
    }

    public HomeItem(String name, String desc, String icon, int id) {
        this.name = name;
        this.desc = desc;
        this.icon = icon;
        this.id = id;
    }
}
