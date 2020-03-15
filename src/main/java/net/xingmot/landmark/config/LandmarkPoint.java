package net.xingmot.landmark.config;

public final class LandmarkPoint{
    public String name;      //显示字符串
    public String color;      //颜色
    public int x,y,z;     //坐标
    public String dimension;//维度
    public String description="";//描述

    public LandmarkPoint(String name, String color, int x, int y, int z, String dimension) {
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.description="";
    }
}