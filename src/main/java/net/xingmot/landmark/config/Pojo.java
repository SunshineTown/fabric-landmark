package net.xingmot.landmark.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Pojo{
    public Map<String,PointGroup> category = new LinkedHashMap<String,PointGroup>();
    public static class PointGroup{
        public Map<String,LandmarkPoint> pointGroups = new LinkedHashMap<String,LandmarkPoint>();
    }
}
