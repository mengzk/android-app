package com.dxm.robotchat.modules.model.entity;

/**
 * Author: Meng
 * Date: 2023/04/26
 * Desc:
 */
public class MusicEntity {
    public int id;
    public String musicUrl;
    public String singer;
    public String cover;
    public String name;
    public String mold;
    public String date;

    public MusicEntity(String singer, String name, String musicUrl) {
        this(0, singer, name, musicUrl);
    }

    public MusicEntity(int id, String singer, String name, String musicUrl) {
        this.musicUrl = musicUrl;
        this.singer = singer;
        this.name = name;
        this.id = id;
    }
}
