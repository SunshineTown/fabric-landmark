package net.xingmot.landmark.config;

import net.minecraft.util.Formatting;

public final class LandmarkPoint{
    public String name;      //显示字符串
    public Formatting color;      //颜色
    public int x,y,z;     //坐标
    public String dimension;//维度
    public String description="";//描述

    public LandmarkPoint(String name, Formatting color, int x, int y, int z, String dimension) {
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.description="";
    }
}