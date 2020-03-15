package net.xingmot.landmark.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Formatting;
import net.minecraft.world.dimension.DimensionType;
import net.xingmot.landmark.LandmarkMod;
import net.xingmot.landmark.config.Pojo.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class PointManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile;

    public PointManager(MinecraftServer server) {
        this.configFile = new File(server.getWorld(DimensionType.OVERWORLD).getSaveHandler().getWorldDir(), "LandmarkPoint.json");
        try {
            loadPoint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPoint() throws IOException {
        if (configFile.exists()) {
            try (Reader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
                LandmarkMod.point = GSON.fromJson(reader, Pojo.class);
            }
        } else {
            configFile.getParentFile().mkdirs();
            LandmarkMod.point=new Pojo();
        }
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
            writer.write(GSON.toJson(LandmarkMod.point,Pojo.class));
        }
    }

    private PointGroup createPointMap(String id, LandmarkPoint point0) {
        HashMap<String,LandmarkPoint> hashMap0= new HashMap<String, LandmarkPoint>();
        PointGroup pointGroup=new PointGroup();
        pointGroup.pointGroups =hashMap0;
        hashMap0.put(id,point0);
        return pointGroup;
    }

    private LandmarkPoint pointNew(String name, Formatting color, int x, int y, int z, String dimension){
        LandmarkPoint point0;
        //"id": { "name": "", "color": "color", "x":x, "y":y, "z":z,"dimension":dimension}
        point0=new LandmarkPoint(name, color, x, y, z, dimension);
        return point0;
    }

    //添加地标
    public void addPoint(String type,String id,String name,Formatting color,int x,int y,int z,String dimension){
        LandmarkPoint pointMap;
        pointMap=pointNew(name, color, x, y, z, dimension);
        if(LandmarkMod.point.category.get(type)==null){
            LandmarkMod.point.category.put(type,createPointMap(id,pointMap));
        }else{
            LandmarkMod.point.category.get(type).pointGroups.put(id,pointMap);
        }
        try {
            savePoint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //删除地标
    public boolean delPoint(String type,String id){
        if(LandmarkMod.point.category.get(type).pointGroups.remove(id)!=null){
            try {
                savePoint();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else{
            return false;
        }
    }
    //保存所有地标到json文件
    public void savePoint() throws IOException{
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
            writer.write(GSON.toJson(LandmarkMod.point));
        }
        loadPoint();
    }

    //改名
    public boolean renamePoint(String type,String id,String name){
        if(LandmarkMod.point.category.get(type).pointGroups.get(id)!=null){
            LandmarkMod.point.category.get(type).pointGroups.get(id).name=name;
            try {
                savePoint();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else{
            return false;
        }
    }

    //改颜色
    public boolean colorPoint(String type,String id, Formatting color){
        if(LandmarkMod.point.category.get(type).pointGroups.get(id)!=null){
            LandmarkMod.point.category.get(type).pointGroups.get(id).color=color;
            try {
                savePoint();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else{
            return false;
        }
    }
}